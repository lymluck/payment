package com.smartstudy.paysdk.callback;

import com.smartstudy.paysdk.enums.PayWay;

import java.io.Serializable;

/**
 * Created by louis on 2017/12/18.
 */

public interface OnPayListener extends Serializable {
    void onPaySuccess(PayWay payWay);

    void onPayCancle(PayWay payWay);

    void onPayFailure(PayWay payWay, int errCode);
}
