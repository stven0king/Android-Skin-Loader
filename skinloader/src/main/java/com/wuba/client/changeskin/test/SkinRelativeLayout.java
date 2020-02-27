package com.wuba.client.changeskin.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wuba.client.changeskin.view.SkinBackgroundAttr;
import com.wuba.client.changeskin.view.SkinViewInterface;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 18:00
 * Description:
 */
public class SkinRelativeLayout extends RelativeLayout implements SkinViewInterface {
    private SkinBackgroundAttr skinBackgroundAttr;
    public SkinRelativeLayout(Context context) {
        this(context, null);
    }

    public SkinRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SkinRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public SkinRelativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        skinBackgroundAttr = new SkinBackgroundAttr(this, attrs);
    }

    @Override
    public void setBackgroundResource(int resid) {
        skinBackgroundAttr.setBackgroundResource(resid);
    }

    @Override
    public void applySkin() {
        skinBackgroundAttr.applySkin();
    }
}
