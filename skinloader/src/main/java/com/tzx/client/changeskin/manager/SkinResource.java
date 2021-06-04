package com.tzx.client.changeskin.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:26
 * Description: 皮肤资源处理
 */
public class SkinResource {
    public static final String ATTR_COLOR = "color";
    public static final String ATTR_DRAWABLE = "drawable";
    public static String getResourceEntryType(Context context, int resId) {
        return context.getResources().getResourceTypeName(resId);
    }
    public static String getResourceEntryType(int resId) {
        return SkinManager.getInstance().getResourceEntryType(resId);
    }
    public static int getColor(Context context, int resId){
        return SkinManager.getInstance().getColor(context, resId);
    }
    public static Drawable getDrawable(Context context, int resId){
        return SkinManager.getInstance().getDrawable(context, resId);
    }

    public static void updateSkinResource(View view) {
        SkinManager.getInstance().updateSkinResource(view);
    }
}
