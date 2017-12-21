package com.smartstudy.zhike;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.smartstudy.paysdk.callback.OnPayListener;
import com.smartstudy.paysdk.enums.PayWay;
import com.smartstudy.paysdk.pay.ZkPayPlatform;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.weChat).setOnClickListener(this);
        findViewById(R.id.aliPay).setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weChat:
                ZkPayPlatform.with(this)
                        .orderId("20171279991219")
                        .token("0nBRuNTHta96XEOHAT1pobV9aY6pmn6F")
                        .payway(PayWay.WXPay).productsName("托福100天")
                        .requestPay(new OnPayListener() {
                            @Override
                            public void onPaySuccess(PayWay way) {
                                Log.d("pay======", "支付成功！");
                                // TODO: 2017/12/20 支付成功后的操作，如展示支付成功页面
                            }

                            @Override
                            public void onPayCancle(PayWay way) {
                                Log.d("pay======", "支付取消！");
                                // TODO: 2017/12/20 支付取消后的操作
                            }

                            @Override
                            public void onPayFailure(PayWay way, int errCode) {
                                Log.d("pay======", "支付失败！");
                                // TODO: 2017/12/20 支付失败后的操作 ，如展示支付失败页面
                            }
                        });
                break;
            case R.id.aliPay:
                ZkPayPlatform.with(this)
                        .token("0nBRuNTHta96XEOHAT1pobV9aY6pmn6F")
                        .payway(PayWay.ALiPay).productsName("托福100天")
                        .requestPay(new OnPayListener() {
                            @Override
                            public void onPaySuccess(PayWay way) {
                                Log.d("pay======", "支付成功！");
                                // TODO: 2017/12/20 支付成功后的操作，如展示支付成功页面
                            }

                            @Override
                            public void onPayCancle(PayWay way) {
                                Log.d("pay======", "支付取消！");
                                // TODO: 2017/12/20 支付取消后的操作
                            }

                            @Override
                            public void onPayFailure(PayWay way, int errCode) {
                                Log.d("pay======", "支付失败！");
                                // TODO: 2017/12/20 支付失败后的操作 ，如展示支付失败页面
                            }
                        });
                break;
            default:
                break;
        }
    }
}
