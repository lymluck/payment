## 使用说明：So easy!

 ### 介绍：
 
        ZkPayPlatform是智课内部使用的Android端支付SDK,目前仅支持微信和支付宝支付.
        
 ### 使用
 
 #### 1.配置：在AndroidManifest.xml中添加微信和支付宝支付的配置文件
       
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
 
 #### 2.集成：
 
 * Gradle
  
        dependencies {
             compile 'com.smartstudy:payment:1.0.0'
        }
        
 * Maven
         
        <dependency>
          <groupId>com.smartstudy</groupId>
          <artifactId>payment</artifactId>
          <version>1.0.0</version>
          <type>pom</type>
        </dependency>
        
  #### 3.支付：
  
        ZkPayPlatform.with(this)
            .orderId("20171279991219").setDebug(true)
            .token("0nBRuNTHta96XEOHAT1pobV9aY6pmn6F")
            .payway(PayWay.WXPay).productsName("托福100天")
            .requestPay(new OnPayListener() {
                 @Override
                 public void onPaySuccess(PayWay way) {
                     Log.d("pay======", "支付成功！");
                 }
       
                 @Override
                 public void onPayCancle(PayWay way) {
                      Log.d("pay======", "支付取消！");
                 }
       
                 @Override
                 public void onPayFailure(PayWay way, int errCode) {
                      Log.d("pay======", "支付失败！");
                 }
            });
  参数说明：<br /><br />
     1. orderId，token，payway为必须参数，sdk有做参数校验，其它的参数非必传;<br /><br />
     2. debug模式表示切换到服务端的测试环境地址
    
  ### 技术支持
 
       作者：louis 
       邮箱：luoyongming@innobuddy.com