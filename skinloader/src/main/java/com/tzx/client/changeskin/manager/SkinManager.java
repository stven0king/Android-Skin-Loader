package com.tzx.client.changeskin.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tzx.client.changeskin.config.SkinConfig;
import com.tzx.client.changeskin.inflater.InflaterInterface;
import com.tzx.client.changeskin.view.SkinViewInterface;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:04
 * Description: 换肤管理器
 */
public class SkinManager implements SkinObservable {
    public static final String TAG = "SkinManager";
    private Context context;
    /**
     * 皮肤包的资源
     */
    private Resources mResources;
    /**
     * 当前皮肤资源的包名
     */
    private String skinPackageName;
    /**
     * 当前皮肤的sdcard路径
     */
    private String skinPath;
    /**
     * 皮肤资源前缀，可空
     */
    private String skinPrefix;
    /**
     * 当前皮肤是否是默认资源
     */
    private boolean isDefaultSkin;
    /**
     * 皮肤观察者集合
     */
    private List<SkinUpdateLinstener> skinObservers;
    /**
     * 注册的自定义LayoutInflater
     */
    private List<InflaterInterface> skinInflaterList = new ArrayList<>();
    /**
     * View转化映射关系
     */
    private Map<String, String> skinInflaterMap = new ArrayMap<>();
    /**
     * 注册的{@link SkinUpdateLinstener}
     * @see SkinManager#markSkinObserverFlag 为真标识容器中的注册的{@link SkinUpdateLinstener}可以换肤，
     * 否则标识识容器中的注册的{@link SkinUpdateLinstener}不可以换肤。
     */
    private List<String> markSkinObservers = new ArrayList<>();
    /**
     * 标识容器中的
     * @see SkinManager#markSkinObservers 可以换肤还是不能换肤
     */
    private boolean markSkinObserverFlag = true;

    private SkinLoaderListener defaultSkinLoaderListener;

    public static SkinManager getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final SkinManager INSTANCE = new SkinManager();
    }

    public SkinManager init(Context context) {
        if (context == null) {
            throw  new IllegalStateException("SkinManager's context is null~!");
        }
        this.context = context;
        skinObservers = new ArrayList<>();
        this.isDefaultSkin = true;
        return this;
    }

    @Override
    public boolean attach(SkinUpdateLinstener observer) {
        if (isSupportSkinUpdateObserver(observer)) {
            if(!skinObservers.contains(observer)){
                skinObservers.add(observer);
                return true;
            }
        }
        return false;
    }

    @Override
    public void detach(SkinUpdateLinstener observer) {
        skinObservers.remove(observer);
    }

    @Override
    public void notifySkinUpdate() {
        for(SkinUpdateLinstener linstener : skinObservers){
            linstener.onSkinChanged();
        }
    }

    public SkinManager setMarkSkinObserverFlag(boolean markSkinObserverFlag) {
        this.markSkinObserverFlag = markSkinObserverFlag;
        return this;
    }

    /**
     * 向markSkinObservers注册{@link SkinUpdateLinstener}名称
     * @param skinUpdateLinstenerName {@link SkinUpdateLinstener}名称
     * @return
     */
    public SkinManager registerMarkSkinObserver(String skinUpdateLinstenerName) {
        if (!TextUtils.isEmpty(skinUpdateLinstenerName)
                && !markSkinObservers.contains(skinUpdateLinstenerName)) {
            markSkinObservers.add(skinUpdateLinstenerName);
        }
        return this;
    }

    /**
     * 向markSkinObservers注册{@link SkinUpdateLinstener}类
     * @param skinUpdateLinstener {@link SkinUpdateLinstener}类
     * @return
     */
    public SkinManager registerMarkSkinObserver(Class<? extends SkinUpdateLinstener> skinUpdateLinstener) {
        if (skinUpdateLinstener == null) return this;
        return registerMarkSkinObserver(skinUpdateLinstener.getName());
    }

    /**
     * 是否支持 {@link SkinUpdateLinstener}
     * @param observer
     * @return
     */
    public boolean isSupportSkinUpdateObserver(SkinUpdateLinstener observer) {
        if (observer == null) return false;
        if (markSkinObserverFlag && markSkinObservers.contains(observer.getClass().getName())
                || (!markSkinObserverFlag && !markSkinObservers.contains(observer.getClass().getName()))) {
            return true;
        }
        return false;
    }

    public boolean isSupportSkinUpdateObserverWithContext(Context context) {
        if (context instanceof SkinUpdateLinstener) {
            return isSupportSkinUpdateObserver((SkinUpdateLinstener)context);
        }
        return false;
    }

    public SkinManager setDefaultSkinLoaderListener(SkinLoaderListener defaultSkinLoaderListener) {
        this.defaultSkinLoaderListener = defaultSkinLoaderListener;
        return this;
    }

    public void load() {
        String defaultSkinPath = SkinConfig.getSkinPath(context);
        if (!TextUtils.isEmpty(defaultSkinPath)) {
            load(defaultSkinPath, SkinConfig.getSkinPrefixPath(context), defaultSkinLoaderListener);
        }
    }

    /**
     * 动态加载皮肤
     * @param skinPackagePath 皮肤文件路径
     */
    public void load(String skinPackagePath) {
        load(skinPackagePath, null, null);
    }

    /**
     * 动态加载皮肤
     * @param skinPackagePath 皮肤文件路径
     * @param skinPrefix 皮肤资源前缀
     */
    public void load(String skinPackagePath, String skinPrefix) {
        load(skinPackagePath, skinPrefix, null);
    }

    /**
     * Load resources from apk in asyc task
     * @param skinPackagePath path of skin apk
     * @param callback callback to notify user
     * @param skinPrefixString skin prefix
     */
    public void load(String skinPackagePath, final String skinPrefixString, final SkinLoaderListener callback) {
        new AsyncTask<String, Void, Resources>() {

            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            };

            @Override
            protected Resources doInBackground(String... params) {
                try {
                    if (params.length == 1) {
                        String skinPkgPath = params[0];
                        File file = new File(skinPkgPath);
                        if(file == null || !file.exists()){
                            return null;
                        }

                        PackageManager mPm = context.getPackageManager();
                        PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                        skinPackageName = mInfo.packageName;

                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);

                        Resources superRes = context.getResources();
                        Resources skinResource = new Resources(assetManager,superRes.getDisplayMetrics(),superRes.getConfiguration());

                        skinPath = skinPkgPath;
                        skinPrefix = skinPrefixString;
                        isDefaultSkin = false;

                        SkinConfig.saveSkinPath(context, skinPath);
                        SkinConfig.saveSkinPrefixPath(context, skinPrefix);
                        return skinResource;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };

            protected void onPostExecute(Resources result) {
                mResources = result;
                if (mResources != null) {
                    if (callback != null) callback.onSuccess();
                    notifySkinUpdate();
                }else{
                    isDefaultSkin = true;
                    if (callback != null) callback.onFailed();
                }
            };

        }.execute(skinPackagePath);
    }

    /**
     * 获取原装resId的类型，
     * @param resId
     * @return color，drawable等等
     */
    public String getResourceEntryType(int resId) {
        if (resId > 0) {
            return context.getResources().getResourceTypeName(resId);
        } else {
            return null;
        }
    }

    /**
     * 获取对应resid的颜色值
     * @param resId
     * @return color的grb数值
     */
    int getColor(Context cxt, int resId) {
        int originColor = cxt.getResources().getColor(resId);
        if(mResources == null || isDefaultSkin || !isSupportSkinUpdateObserverWithContext(cxt)){
            return originColor;
        }
        String resName = context.getResources().getResourceEntryName(resId);
        int trueResId = mResources.getIdentifier(getSkinResourceName(resName), "color", skinPackageName);
        int trueColor = 0;
        try{
            trueColor = mResources.getColor(trueResId);
        }catch(Resources.NotFoundException e){
            Log.d(TAG, "color can not skin peeler that name is " + resName);
            trueColor = originColor;
        }
        return trueColor;
    }

    /**
     * 获取对应resid的图片
     * @param resId
     * @return 对应的图片
     */
    Drawable getDrawable(Context cxt, int resId){
        Drawable originDrawable = cxt.getResources().getDrawable(resId);
        if(mResources == null || isDefaultSkin || !isSupportSkinUpdateObserverWithContext(cxt)){
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);
        int trueResId = mResources.getIdentifier(getSkinResourceName(resName), "drawable", skinPackageName);
        Drawable trueDrawable = null;
        try{
            trueDrawable = mResources.getDrawable(trueResId);
        }catch(Resources.NotFoundException e){
            Log.d(TAG, "drawable can not skin peeler that name is " + resName);
            trueDrawable = originDrawable;
        }
        return trueDrawable;
    }

    /**
     * 更新当前View的皮肤
     * @param rootView
     */
    void updateSkinResource(View rootView) {
        if (rootView != null) {
            if (rootView instanceof SkinViewInterface) {
                ((SkinViewInterface) rootView).applySkin();
            }
            if (rootView instanceof ViewGroup) {
                int count = ((ViewGroup) rootView).getChildCount();
                for (int i = 0; i < count; i++) {
                    updateSkinResource(((ViewGroup) rootView).getChildAt(i));
                }
            }
        }
    }

    /**
     * 获取目前的资源包的资源名
     * @param resName
     * @return
     */
    private String getSkinResourceName(String resName) {
        if (TextUtils.isEmpty(resName) || TextUtils.isEmpty(skinPrefix)) {
            return resName;
        }
        return skinPrefix + resName;
    }

    /**
     * 使用默认资源皮肤
     */
    public void setDefaultSkin() {
        isDefaultSkin = true;
        mResources = null;
        notifySkinUpdate();
    }

    public List<InflaterInterface> getSkinInflaterList() {
        return skinInflaterList;
    }

    /**
     * 注册自定义的LayoutInflater，
     * @param inflaterInterface
     */
    public void registSkinInflater(InflaterInterface inflaterInterface) {
        skinInflaterList.add(inflaterInterface);
    }

    /**
     * 注册可以进行换肤的自定义View，该View实现 {@link SkinViewInterface}
     * @param viewName view的名称
     * @return
     */
    public SkinManager registSkinViewConvertMap(String viewName) {
        if (!TextUtils.isEmpty(viewName)) {
            skinInflaterMap.put(viewName, viewName);
        }
        return this;
    }

    /**
     * 注册可以进行换肤的自定义View，该View实现 {@link SkinViewInterface}
     * @param viewClass view的类型
     * @return
     */
    public SkinManager registSkinViewConvertMap(Class<? extends SkinViewInterface> viewClass) {
        return registSkinViewConvertMap(viewClass.getName(), viewClass);
    }

    /**
     * 注册可以进行换肤的自定义View，该View实现 {@link SkinViewInterface}
     * @param viewClass 需要转化的view的类型
     * @param toViewClass 转化之后的view的类型
     * @return
     */
    public SkinManager registSkinViewConvertMap(Class<? extends View> viewClass, Class<? extends SkinViewInterface> toViewClass) {
        return registSkinViewConvertMap(viewClass.getName(), toViewClass);
    }

    /**
     * 注册可以进行换肤的自定义View，该View实现 {@link SkinViewInterface}
     * @param viewClass 需要转化的view的类名
     * @param viewClass 转化之后的view的类型
     * @return
     */
    public SkinManager registSkinViewConvertMap(String viewName, Class<? extends SkinViewInterface> viewClass) {
        if (!TextUtils.isEmpty(viewName)
                && viewClass != null
                && !skinInflaterMap.containsKey(viewName)) {
            if (!viewClass.getName().equals(viewName)) {
                registSkinViewConvertMap(viewClass);
            }
            skinInflaterMap.put(viewName, viewClass.getName());
        }
        return this;
    }

    /**
     * 注册可以进行换肤的自定义View，该View实现 {@link SkinViewInterface}
     * @param skinInflaterMap key=需要转化的view的类名，value=转化之后的view的类型
     * @return
     */
    public SkinManager registSkinViewConvertMap(Map<String, Class<? extends SkinViewInterface>> skinInflaterMap) {
        if (skinInflaterMap == null || skinInflaterMap.size() == 0) return this;
        Iterator<Map.Entry<String, Class<? extends SkinViewInterface>>> entries = skinInflaterMap.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<String, Class<? extends SkinViewInterface>> entry = entries.next();
            String key = entry.getKey();
            Class<? extends SkinViewInterface> value = entry.getValue();
            registSkinViewConvertMap(key, value);
        }
        return this;
    }

    /**
     * 该view是否可以转化为换肤View
     * @param name view的名称
     * @return
     */
    public boolean canSkinViewConvert(String name) {
        return skinInflaterMap.containsKey(name);
    }

    /**
     * 该view是否可以转化为换肤View
     * @param cls view的类型
     * @return
     */
    public boolean canSkinViewConvert(Class<? extends View> cls) {
        if (cls == null) {
            return false;
        }
        return skinInflaterMap.containsKey(cls.getName());
    }

    /**
     * 该view是否可以转化为换肤View
     * @param name view的名称
     * @return
     */
    public String findSkinInflaterViewClassName(String name) {
        return skinInflaterMap.get(name);
    }

}
