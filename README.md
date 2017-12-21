### 使用说明：So easy!
 * 1.配置：在AndroidManifest.xml中添加微信和支付宝支付的配置文件
       
        <!-- 微信支付 sdk ，也是 pay sdk 调用入口 -->
        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.smartstudy.paysdk.PaymentActivity" />
        <!-- 支付宝 sdk -->
        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
        <activity
            android:name="com.alipay.sdk.app.H5AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
 * 2.使用：导入项目中的模块payment，在支付时调用如下方法
 
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
    
 * 3.技术支持
 
       luoyongming@innobuddy.com