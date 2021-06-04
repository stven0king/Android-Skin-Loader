package com.tzx.client.changeskin.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

import com.tzx.client.changeskin.view.SkinBackgroundAttr;
import com.tzx.client.changeskin.view.SkinTextColorAttr;
import com.tzx.client.changeskin.view.SkinViewInterface;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 18:00
 * Description:
 */
public class SkinButton extends Button implements SkinViewInterface {
    private SkinBackgroundAttr skinBackgroundAttr;
    private SkinTextColorAttr skinTextColorAttr;
    public SkinButton(Context context) {
        this(context, null);
    }

    public SkinButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SkinButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public SkinButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        skinBackgroundAttr = new SkinBackgroundAttr(this, attrs);
        skinTextColorAttr = new SkinTextColorAttr(this, attrs);
    }

    @Override
    public void setBackgroundResource(int resid) {
        skinBackgroundAttr.setBackgroundResource(resid);
    }

    @Override
    public void applySkin() {
        skinBackgroundAttr.applySkin();
        skinTextColorAttr.applySkin();
    }
}
