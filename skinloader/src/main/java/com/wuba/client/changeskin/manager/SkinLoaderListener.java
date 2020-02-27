package com.wuba.client.changeskin.manager;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:17
 * Description:资源包加载的回调监听
 */
public interface SkinLoaderListener {
    void onStart();
    void onSuccess();
    void onFailed();
    SkinLoaderListener DEFAULT_SKIN_CHANGING_CALLBACK = new SkinLoaderListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailed() {

        }
    };
}
