package com.tzx.client.changeskin.manager;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-20 16:06
 * Description: 可以观察皮肤变换的管理者
 */
public interface SkinObservable {
    /**
     * 添加观察者
     * @param observer 观察皮肤变化
     * @return 是否添加成功
     */
    boolean attach(SkinUpdateLinstener observer);

    /**
     * 删除观察者
     * @param observer 观察皮肤变化
     */
    void detach(SkinUpdateLinstener observer);

    /**
     * 皮肤变更
     */
    void notifySkinUpdate();
}
