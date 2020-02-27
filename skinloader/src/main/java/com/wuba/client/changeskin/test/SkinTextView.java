package com.wuba.client.changeskin.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wuba.client.changeskin.view.SkinBackgroundAttr;
import com.wuba.client.changeskin.view.SkinTextColorAttr;
import com.wuba.client.changeskin.view.SkinViewInterface;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 18:01
 * Description:
 */
public class SkinTextView extends TextView implements SkinViewInterface {
    private SkinBackgroundAttr skinBackgroundAttr;
    private SkinTextColorAttr skinTextColorAttr;
    public SkinTextView(Context context) {
        this(context, null);
    }

    public SkinTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SkinTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public SkinTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        skinBackgroundAttr = new SkinBackgroundAttr(this, attrs);
        skinTextColorAttr = new SkinTextColorAttr(this, attrs);
    }

    @Override
    public void setBackgroundResource(int resid) {
        skinBackgroundAttr.setBackgroundResource(resid);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        //skinTextColorAttr.setTextColor(color);
    }

    @Override
    public void applySkin() {
        skinTextColorAttr.applySkin();
        skinBackgroundAttr.applySkin();
    }
}
