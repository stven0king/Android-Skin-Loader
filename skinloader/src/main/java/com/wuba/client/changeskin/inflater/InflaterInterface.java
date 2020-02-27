package com.wuba.client.changeskin.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-21 09:33
 * Description:inflater的接口
 */
public interface InflaterInterface {
    View onCreateView(View parent, String name, Context context, AttributeSet attrs);
}
