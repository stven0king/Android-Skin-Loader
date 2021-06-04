package com.tzx.client.changeskin.config;

import android.content.Context;
import android.util.Log;

import com.tzx.client.changeskin.manager.SkinPreferences;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:23
 * Description: 皮肤的配置
 */
public class SkinConfig {
    //默认的命名空间
    public  static final String NAMESPACE = "http://schemas.android.com/android/skin";
    //自定义的命名空间
    public static final String AUTO_NAMESPACE = "http://schemas.android.com/apk/res-auto";
    public static final String PREF_CUSTOM_SKIN_PATH = "tanzhenxing_skin_custom_path";
    public static final String PREF_CUSTOM_SKIN_PREFIX_PATH = "tanzhenxing_skinPrefix_custom_path";
    public static final String ATTR_SKIN_ENABLE = "attr_skin_enable";

    /**
     * get path of last skin package path
     * @param context
     * @return path of skin package
     */
    public static String getSkinPath(Context context){
        Log.d("SkinManager", "getSkinPath");
        return SkinPreferences.getString(context, PREF_CUSTOM_SKIN_PATH, null);
    }

    public static void saveSkinPath(Context context, String path){
        Log.d("SkinManager", "saveSkinPath=" + path);
        SkinPreferences.putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    /**
     * get path of last skin prefix
     * @param context
     * @return path of skin prefix
     */
    public static String getSkinPrefixPath(Context context){
        Log.d("SkinManager", "getSkinPrefixPath");
        return SkinPreferences.getString(context, PREF_CUSTOM_SKIN_PREFIX_PATH, null);
    }

    public static void saveSkinPrefixPath(Context context, String path){
        Log.d("SkinManager", "saveSkinPrefixPath=" + path);
        SkinPreferences.putString(context, PREF_CUSTOM_SKIN_PREFIX_PATH, path);
    }
}
