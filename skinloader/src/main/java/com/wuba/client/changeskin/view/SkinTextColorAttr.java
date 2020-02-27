package com.wuba.client.changeskin.view;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wuba.client.changeskin.manager.SkinResource;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:49
 * Description: textColor的属性标签换肤
 */
public class SkinTextColorAttr extends AbstraceSkinAttr{

    public SkinTextColorAttr(View view, AttributeSet attrs) {
        super(view, attrs);
        resourceId = getAttributeValue(attrs, android.R.attr.textColor);
    }

    @Override
    protected Class<? extends View> getSkinViewClass() {
        return TextView.class;
    }

    @Override
    public void applySkinWithValid() {
        if (SkinResource.ATTR_COLOR.equals(SkinResource.getResourceEntryType(resourceId))) {
            ((TextView) view).setTextColor(SkinResource.getColor(view.getContext(), resourceId));
        }
    }

    //public void setTextColor(int resid) {
    //    resourceId = checkResourceId(resid);
    //    applySkin();
    //}
}
