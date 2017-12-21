package com.smartstudy.paysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.smartstudy.paysdk.controller.PayController;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import static com.smartstudy.paysdk.util.ConstantUtils.WECHAT_PAY_COMMAND;
import static com.smartstudy.paysdk.util.ConstantUtils.WECHAT_PAY_RESULT_ACTION;
import static com.smartstudy.paysdk.util.ConstantUtils.WECHAT_PAY_RESULT_EXTRA;


/**
 * @author louis
 */
public class PaymentActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = PayController.getInstance().getWXapi();
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 微信支付结果处理
     *
     * @param baseResp
     */
    @Override
    public void onResp(final BaseResp baseResp) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        Intent payResult = new Intent();
        payResult.setAction(WECHAT_PAY_RESULT_ACTION);
        payResult.putExtra(WECHAT_PAY_RESULT_EXTRA, baseResp.errCode);
        payResult.putExtra(WECHAT_PAY_COMMAND, baseResp.getType());
        broadcastManager.sendBroadcast(payResult);
        finish();
    }

}
