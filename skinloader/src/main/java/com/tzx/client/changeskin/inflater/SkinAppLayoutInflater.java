package com.tzx.client.changeskin.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tzx.client.changeskin.manager.SkinManager;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-21 17:06
 * Description: 默认自定义view配置解析
 */
public class SkinAppLayoutInflater extends AbsLayoutInflater implements InflaterInterface {
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (-1 == name.indexOf('.')) {
            for (int i = 0; i < sClassPrefixList.length; i++) {
                if (SkinManager.getInstance().canSkinViewConvert(sClassPrefixList[i] + name)) {
                    name = sClassPrefixList[i] + name;
                    return createViewFromTag(context, SkinManager.getInstance().findSkinInflaterViewClassName(name), attrs);
                }
            }
        }
        if (SkinManager.getInstance().canSkinViewConvert(name)) {
            return createViewFromTag(context, SkinManager.getInstance().findSkinInflaterViewClassName(name), attrs);
        } else {
            return null;
        }
    }
}
