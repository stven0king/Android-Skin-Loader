package com.tzx.client.changeskin.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.tzx.client.changeskin.manager.SkinManager;
import com.tzx.client.changeskin.view.SkinViewInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-21 09:30
 * Description: 管理Inflater
 */
public class SkinInflaterFactory extends AbsLayoutInflater implements LayoutInflater.Factory2 {
    private SkinAppLayoutInflater appLayoutInflater;
    private List<WeakReference<SkinViewInterface>> skinView = new ArrayList<>();

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createViewWithAttr(parent ,name, context, attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return createViewWithAttr(null, name, context, attrs);
    }

    /**
     * 创建根据View的名称和属性标签
     * @param parent 父View
     * @param name View的名称
     * @param context 上下文环境
     * @param attrs view的属性标签
     * @return
     */
    private View createViewWithAttr(View parent, String name, Context context, AttributeSet attrs) {
        View view = createViewFromInflater(null, name, context, attrs);
        if (view == null) {
            if (appLayoutInflater == null) {
                appLayoutInflater = new SkinAppLayoutInflater();
            }
            view = appLayoutInflater.onCreateView(parent, name, context, attrs);
        }
        if (view instanceof SkinViewInterface) {
            skinView.add(new WeakReference<>((SkinViewInterface) view));
            ((SkinViewInterface) view).applySkin();
        }
        return view;
    }

    /**
     * 利用自定义的LayoutInflater反射构造自定义View
     * @param parent 父View
     * @param name 类名
     * @param context 上下文
     * @param attrs view的属性标签
     * @return
     */
    private View createViewFromInflater(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        for (InflaterInterface inflater : SkinManager.getInstance().getSkinInflaterList()) {
            view = inflater.onCreateView(parent, name, context, attrs);
            if (view != null) {
                return view;
            }
        }
        return view;
    }

    public void applySkin() {
        if (skinView != null && !skinView.isEmpty()) {
            for (WeakReference ref : skinView) {
                if (ref != null && ref.get() != null) {
                    ((SkinViewInterface) ref.get()).applySkin();
                }
            }
        }
    }
}
