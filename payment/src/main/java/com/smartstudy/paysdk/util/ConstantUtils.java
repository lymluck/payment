package com.smartstudy.paysdk.util;

/**
 * Created by louis on 2017/12/8.
 */

public class ConstantUtils {

    //建立连接超时时间 5s
    public static int CONNECTION_TIMEOUT = 5;
    //读取超时 10s
    public static int READ_TIMEOUT = 10;
    //写入超时 30s
    public static int WRITE_TIMEOUT = 30;
    public static final int ERR_OTHER = 99999;
    public static final int WECHAT_NOT_INSTALLED_ERR = -5;
    public static final int WECHAT_UNSUPPORT_ERR = -6;
    //获取信息失败提示信息
    public static final String GET_DATA_FAILED = "获取数据失败,请稍候重试!";
    public static final String WECHAT_PAY_RESULT_ACTION = "com.tencent.mm.opensdk.WECHAT_PAY_RESULT_ACTION";
    public static final String WECHAT_PAY_RESULT_EXTRA = "com.tencent.mm.opensdk.WECHAT_PAY_RESULT_EXTRA";
    public static final String WECHAT_PAY_COMMAND = "com.tencent.mm.opensdk.WECHAT_PAY_COMMAND";

    public static String URL_WX_PAY = "/pay/wechat/app/%1$s";
    public static String URL_ALI_PAY = "/pay/alipay/app/%1$s";
    public static String URL_VALIDATE_PAY = "/pay/%1$s/validate";
    public static String URL_PAY_RESULT = "/pay/%1$s/result";

    /*********获取api接口url***********/
    public static String getUrl(boolean debug, String url) {
        return debug ? "http://api.staging.smartstudy.com/sdk" + url : "api.smartstudy.com/sdk" + url;
    }
}
