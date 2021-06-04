package com.tzx.client.changeskin.view;

import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:52
 * Description: view的换肤属性的基类
 * 提供
 */
public abstract class AbstraceSkinAttr implements SkinViewInterface {
    protected static final String SYSTEM_ID_PREFIX = "1";
    public static final int INVALID_ID = 0;
    protected int resourceId = INVALID_ID;
    protected View view;

    AbstraceSkinAttr(View view, AttributeSet attrs) {
        this.view = view;
    }

    /**
     * 检查id是否有效
     * @param resId 资源id
     * @return
     */
    int checkResourceId(int resId) {
        String hexResId = Integer.toHexString(resId);
        return hexResId.startsWith(SYSTEM_ID_PREFIX) ? INVALID_ID : resId;
    }

    /**
     * 获取对应资源id的属性值，获取来源依次为xml布局，style，theme
     * @param attr 构成View的属性参数
     * @param paramInt android.R.attr.background
     * @return
     */
    int getAttributeValue(AttributeSet attr, int paramInt) {
        if (attr == null) return INVALID_ID;
        int value = INVALID_ID;
        int count = attr.getAttributeCount();
        for(int i = 0; i <count;i++) {
            if(attr.getAttributeNameResource(i) == paramInt) {
                String str = attr.getAttributeValue(i);
                if(isReferenceTypes(str)) {
                    return stringConvertInteger(str);
                }
            }
        }
        TypedArray ta = null;
        try {
            int styleId = attr.getAttributeResourceValue(null, "style", 0);
            ta = view.getContext().getTheme().obtainStyledAttributes(attr, new int[]{paramInt}, styleId, 0);
            int id = ta.getResourceId(0, INVALID_ID);
            if (id != INVALID_ID) {
                value = id;
            }
            ta.recycle();
        } catch (Exception e) {
            if (ta != null) {
                ta.recycle();
            }
            e.printStackTrace();
        }
        return value;
    }

    int getAttributeValueWithName(AttributeSet attr, String attrName) {
        int value = -1;
        int count = attr.getAttributeCount();
        for(int i = 0; i <count;i++) {
            if(attrName.equals(attr.getAttributeName(i))) {
                String str = attr.getAttributeValue(i);
                if(isReferenceTypes(str)) {
                    return stringConvertInteger(str);
                }
            }
        }
        return value;
    }

    /**
     * 当前的SkinAttr的目标View的基类
     * @return View的Class
     */
    protected abstract Class<? extends View> getSkinViewClass();

    /**
     * 判断资源是否为引用类型
     * ?0x99d99f8f8
     * @0x7f010000
     * @param value id的字符串类型
     * @return true为有效
     */
    private boolean isReferenceTypes(String value) {
        return !TextUtils.isEmpty(value) && (value.startsWith("?") || value.startsWith("@"));
    }

    /**
     * id的16进制字符串类型转化为数字
     * ?0x99d99f8f8
     * @0x7f010000
     * @param value id的16进制字符串
     * @return 转化后的数字
     */
    private int stringConvertInteger(String value) {
        int result = INVALID_ID;
        try {
            result = Integer.parseInt(value.substring(1, value.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void applySkin() {
        if (resourceId != INVALID_ID && getSkinViewClass().isAssignableFrom(view.getClass())) {
            applySkinWithValid();
        }
    }

    /**
     * 有效换肤
     */
    public abstract void applySkinWithValid();
}
