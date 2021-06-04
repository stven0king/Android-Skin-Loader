package com.tzx.client.changeskin.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;


import com.tzx.client.changeskin.inflater.SkinInflaterFactory;
import com.tzx.client.changeskin.manager.SkinManager;
import com.tzx.client.changeskin.manager.SkinResource;
import com.tzx.client.changeskin.manager.SkinUpdateLinstener;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 17:58
 * Description:
 */
public class SkinBaseActivity extends Activity implements SkinUpdateLinstener {
    public SkinInflaterFactory skinInflaterFactory;
    protected boolean isSupportSkinupdate = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (SkinManager.getInstance().isSupportSkinUpdateObserver(this)) {
            skinInflaterFactory = new SkinInflaterFactory();
            LayoutInflater.from(this).setFactory2(skinInflaterFactory);
            isSupportSkinupdate = true;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSupportSkinupdate) {
            SkinManager.getInstance().attach(this);
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    SkinResource.updateSkinResource(getWindow().getDecorView());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isSupportSkinupdate) {
            SkinManager.getInstance().detach(this);
        }
    }

    @Override
    public void onSkinChanged() {
        if (isSupportSkinupdate) {
            SkinResource.updateSkinResource(getWindow().getDecorView());
            skinInflaterFactory.applySkin();
        }
    }
}
