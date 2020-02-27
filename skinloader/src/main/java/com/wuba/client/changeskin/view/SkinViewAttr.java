package com.wuba.client.changeskin.view;

/**
 * Created by Tanzhenxing
 * Date: 2020-02-24 17:50
 * Description:
 */
public class SkinViewAttr {
    public static void applySkin(SkinViewInterface... arr) {
        if (arr != null && arr.length > 0) {
            for (SkinViewInterface viewInterface: arr) {
                if (viewInterface != null) {
                    viewInterface.applySkin();
                }
            }
        }
    }
}
