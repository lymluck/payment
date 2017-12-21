package com.smartstudy.paysdk.pay;

import android.app.Activity;

import com.smartstudy.paysdk.controller.PayController;

/**
 * Created by louis on 2017/12/15.
 */

public class ZkPayPlatform {

    /**
     * 智课支付SDK
     *
     * @param context
     * @return
     */
    public static PayController with(Activity context) {
        PayController controller = PayController.getInstance();
        controller.get(context);
        return controller;
    }
}
