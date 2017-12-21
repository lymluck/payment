package com.smartstudy.paysdk.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.smartstudy.paysdk.bean.AliPayBean;
import com.smartstudy.paysdk.bean.WXPayBean;
import com.smartstudy.paysdk.callback.Callback;
import com.smartstudy.paysdk.callback.OnPayListener;
import com.smartstudy.paysdk.callback.ServerResultCallback;
import com.smartstudy.paysdk.enums.PayWay;
import com.smartstudy.paysdk.model.PayModel;
import com.smartstudy.paysdk.util.ConstantUtils;
import com.smartstudy.paysdk.util.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import static com.smartstudy.paysdk.util.ConstantUtils.WECHAT_PAY_COMMAND;
import static com.smartstudy.paysdk.util.ConstantUtils.WECHAT_PAY_RESULT_ACTION;
import static com.smartstudy.paysdk.util.ConstantUtils.WECHAT_PAY_RESULT_EXTRA;

/**
 * Created by louis on 2017/12/15.
 */

public class PayController {
    private LocalBroadcastManager mBroadcastManager;
    private OnPayListener mPayListener;
    private Activity mContext;
    private String mOrderId;
    private String mToken;
    private String mProductsName;
    private PayWay mPayWay;
    private String mSerial;
    public static PayController mInstance;

    public PayController(Activity context) {
        this.mContext = context;
    }

    public static PayController getInstance(Activity context) {
        if (null == mInstance) {
            mInstance = new PayController(context);
        }
        return mInstance;
    }

    public PayController payway(PayWay payWay) {
        this.mPayWay = payWay;
        return this;
    }

    public PayController orderId(String orderId) {
        this.mOrderId = orderId;
        return this;
    }

    public PayController token(String token) {
        this.mToken = token;
        return this;
    }

    public PayController productsName(String productsName) {
        this.mProductsName = productsName;
        return this;
    }

    /**
     * 支付
     *
     * @param onPayListener
     */
    public void requestPay(final OnPayListener onPayListener) {
        checkArgs();
        PayModel.requestPay(mPayWay, mOrderId, mToken, mProductsName, new Callback<String>() {
            @Override
            public void onErr(String msg) {
                ToastUtils.shortToast(mContext, msg);
            }

            @Override
            public void onSuccess(String result) {
                switch (mPayWay) {
                    case WXPay:
                        WXPayBean wxData = JSON.parseObject(result, WXPayBean.class);
                        if (wxData != null) {
                            mSerial = wxData.getSerial();
                            goWXPay(wxData, onPayListener);
                        } else {
                            ToastUtils.shortToast(mContext, ConstantUtils.GET_DATA_FAILED);
                        }
                        break;
                    case ALiPay:
                        AliPayBean aliData = JSON.parseObject(result, AliPayBean.class);
                        if (aliData != null) {
                            mSerial = aliData.getSerial();
                            goAliPay(aliData, onPayListener);
                        } else {
                            ToastUtils.shortToast(mContext, ConstantUtils.GET_DATA_FAILED);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 验证支付结果
     *
     * @param context
     * @param callback
     */
    private void verifyPayResult(final Context context, final ServerResultCallback callback) {
        PayModel.verifyPayResult(mOrderId, mToken, mSerial, new Callback<String>() {
            @Override
            public void onErr(String msg) {
                ToastUtils.shortToast(context, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                if (data.getBooleanValue("done")) {
                    callback.onPaid();
                } else {
                    callback.unPaid();
                }
            }
        });
    }

    /**
     * 调起微信支付
     *
     * @param result
     * @param onPayListener
     */
    private void goWXPay(WXPayBean result, OnPayListener onPayListener) {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, result.getAppid(), true);
        PayReq req = new PayReq();
        req.appId = result.getAppid();
        req.partnerId = result.getPartnerid();
        req.prepayId = result.getPrepayid();
        req.packageValue = result.getPackageX();
        req.nonceStr = result.getNoncestr();
        req.timeStamp = result.getTimestamp();
        req.sign = result.getSign();
        api.registerApp(result.getAppid());
        registPayResultBroadcast(mContext, onPayListener);
        api.sendReq(req);
    }

    /**
     * 调起支付宝支付
     *
     * @param payBean
     * @param onPayListener
     */
    private void goAliPay(final AliPayBean payBean, final OnPayListener onPayListener) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(payBean.getPrepayInfo(), true);
                onAliPayRes(result, onPayListener);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付结果处理
     *
     * @param payResult
     * @param onPayListener
     */
    private void onAliPayRes(Map<String, String> payResult, final OnPayListener onPayListener) {
        //验证支付结果
        final String status = payResult.get("resultStatus");
        if ("9000".equals(status) || "8000".equals(status)) {
            //订单支付成功，验证结果
            verifyPayResult(mContext, new ServerResultCallback() {
                @Override
                public void unPaid() {
                    //客户端成功，服务端失败
                    onPayListener.onPayFailure(PayWay.ALiPay, 8000);
                }

                @Override
                public void onPaid() {
                    onPayListener.onPaySuccess(PayWay.ALiPay);
                }
            });
        } else if ("6001".equals(status)) {
            onPayListener.onPayCancle(PayWay.ALiPay);
        } else {
            onPayListener.onPayFailure(PayWay.ALiPay, TextUtils.isDigitsOnly(status) ? Integer.parseInt(status) : ConstantUtils.ERR_OTHER);
        }
    }

    /**
     * 注册接收数据广播
     *
     * @param context
     * @param onPayListener
     */
    private void registPayResultBroadcast(Context context, OnPayListener onPayListener) {
        mBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        mPayListener = onPayListener;
        IntentFilter filter = new IntentFilter(WECHAT_PAY_RESULT_ACTION);
        mBroadcastManager.registerReceiver(mReceiver, filter);
    }

    /**
     * 移除广播
     */
    private void unRegistPayResultBroadcast() {
        if (mBroadcastManager != null && mReceiver != null) {
            mBroadcastManager.unregisterReceiver(mReceiver);
            mBroadcastManager = null;
            mReceiver = null;
            mContext = null;
            mPayListener = null;
        }
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int result = intent.getIntExtra(WECHAT_PAY_RESULT_EXTRA, -100);
            int type = intent.getIntExtra(WECHAT_PAY_COMMAND, -1000);
            //验证支付结果
            if (type == ConstantsAPI.COMMAND_PAY_BY_WX) {
                if (result == 0) {
                    //订单支付成功，验证结果
                    verifyPayResult(context, new ServerResultCallback() {
                        @Override
                        public void unPaid() {
                            //客户端成功，服务端失败
                            mPayListener.onPayFailure(PayWay.WXPay, result);
                        }

                        @Override
                        public void onPaid() {
                            mPayListener.onPaySuccess(PayWay.WXPay);
                        }
                    });
                } else if (result == -2) {
                    mPayListener.onPayCancle(PayWay.WXPay);
                } else {
                    mPayListener.onPayFailure(PayWay.WXPay, result);
                }
            }
            unRegistPayResultBroadcast();
        }
    };

    /**
     * 参数校验
     */
    private void checkArgs() {
        if (TextUtils.isEmpty(mPayWay.toString())) {
            throw new IllegalArgumentException("Please specify the \"payway\" of payment!");
        } else if (TextUtils.isEmpty(mOrderId)) {
            throw new IllegalArgumentException("Please specify the \"orderId\" of payment!");
        } else if (TextUtils.isEmpty(mToken)) {
            throw new IllegalArgumentException("Please specify the \"token\" of payment!");
        }
    }
}
