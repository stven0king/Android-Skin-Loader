package com.tzx.client.changeskin.view;

import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tzx.client.changeskin.manager.SkinManager;
import com.tzx.client.changeskin.manager.SkinResource;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:49
 * Description: background的属性标签换肤
 */
public class SkinBackgroundAttr extends AbstraceSkinAttr{
    public SkinBackgroundAttr(View view, AttributeSet attrs) {
        super(view, attrs);
        resourceId = getAttributeValue(attrs , android.R.attr.background);
    }

    @Override
    protected Class<? extends View> getSkinViewClass() {
        return View.class;
    }

    @Override
    public void applySkinWithValid() {
        String resourceEntryType = SkinResource.getResourceEntryType(resourceId);
        if (SkinResource.ATTR_COLOR.equals(resourceEntryType)) {
            view.setBackgroundColor(SkinResource.getColor(view.getContext(), resourceId));
        } else if (SkinResource.ATTR_DRAWABLE.equals(resourceEntryType)){
            view.setBackgroundDrawable(SkinResource.getDrawable(view.getContext(), resourceId));
        } else {
            Log.e(SkinManager.TAG, "SkinBackgroundAttr error resourceEntryType:" + resourceEntryType);
        }
    }

    public void setBackgroundResource(@DrawableRes int resid) {
        resourceId = checkResourceId(resid);
        applySkinWithValid();
    }
}
