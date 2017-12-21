package com.smartstudy.paysdk.enums;

/**
 * Created by Louis on 2017/3/11.
 */

public enum PayWay {
    WXPay(0),
    ALiPay(1);

    int payway;

    PayWay(int way) {
        payway = way;
    }

    @Override
    public String toString() {
        return String.valueOf(payway);
    }

}
