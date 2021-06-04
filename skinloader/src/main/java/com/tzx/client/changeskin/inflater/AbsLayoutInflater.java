package com.tzx.client.changeskin.inflater;

import android.content.Context;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;


import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-21 17:01
 * Description: 自定义View解析的基类
 */
public abstract class AbsLayoutInflater implements InflaterInterface {
    protected static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };
    protected static final Class<?>[] sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
    protected static final Map<String, Constructor<? extends View>> sConstructorMap = new ArrayMap<>();
    protected final Object[] mConstructorArgs = new Object[2];

    /**
     * 创建View构造
     * @param context 上线文
     * @param name view名称
     * @param attrs view的属性标签
     * @return
     */
    protected View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if ("view".equals(name)) {
            name = attrs.getAttributeValue(null, "class");
        }
        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;
            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createView(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    /**
     * 反射构造View
     * @param context 上下文环境
     * @param name View名称
     * @param prefix View的包名
     * @return
     * @throws ClassNotFoundException
     * @throws InflateException
     */
    private View createView(Context context, String name, String prefix) throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);
                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            e.printStackTrace();
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }
}
