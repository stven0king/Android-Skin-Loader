package com.tzx.client.changeskin.view;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tzx.client.changeskin.manager.SkinResource;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-24 17:42
 * Description:处理android:drawableLeft="@drawable/icon_comp_title_left"
 */
public class SkinDrawableLeftAttr extends AbstraceSkinAttr {
    public SkinDrawableLeftAttr(View view, AttributeSet attrs) {
        super(view, attrs);
        resourceId = getAttributeValue(attrs , android.R.attr.drawableLeft);
    }

    @Override
    protected Class<? extends View> getSkinViewClass() {
        return TextView.class;
    }

    @Override
    public void applySkinWithValid() {
        Drawable drawable = SkinResource.getDrawable(view.getContext(), resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        ((TextView)view).setCompoundDrawables(drawable, null, null, null);
        //textView.setCompoundDrawablePadding(dip2px(5)) ;
    }
}
