package com.smartstudy.paysdk.model;

import android.text.TextUtils;

import com.smartstudy.paysdk.callback.Callback;
import com.smartstudy.paysdk.config.RequestConfig;
import com.smartstudy.paysdk.controller.PayController;
import com.smartstudy.paysdk.enums.PayWay;
import com.smartstudy.paysdk.server.RequestServer;
import com.smartstudy.paysdk.util.ConstantUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 2017/12/8.
 */
public class PayModel {

    public static void requestPay(final PayWay payWay, final String orderId, final String token, final String productsName, Callback callback) {
        RequestServer.getInstance().doPost(new RequestConfig<String>() {
            @Override
            public String getUrl() {
                String url = "";
                switch (payWay) {
                    case WXPay:
                        url = ConstantUtils.getUrl(PayController.getInstance().isDebug(), String.format(ConstantUtils.URL_WX_PAY, orderId));
                        break;
                    case ALiPay:
                        url = ConstantUtils.getUrl(PayController.getInstance().isDebug(), String.format(ConstantUtils.URL_ALI_PAY, orderId));
                        break;
                    default:
                        break;
                }
                return url;
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("token", token);
                if (!TextUtils.isEmpty(productsName)) {
                    params.put("productsName", productsName);
                }
                return params;
            }
        }, callback);
    }

    public static void verifyPayResult(final String orderId, final String token, final String serial, Callback callback) {
        RequestServer.getInstance().doGet(new RequestConfig<String>() {
            @Override
            public String getUrl() {
                return ConstantUtils.getUrl(PayController.getInstance().isDebug(), String.format(ConstantUtils.URL_PAY_RESULT, orderId));
            }

            @Override
            public Map getParams() {
                Map params = new HashMap();
                params.put("token", token);
                params.put("serial", serial);
                return params;
            }
        }, callback);
    }
}
