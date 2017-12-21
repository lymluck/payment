package com.smartstudy.paysdk.server;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.smartstudy.paysdk.bean.ResponseBean;
import com.smartstudy.paysdk.callback.Callback;
import com.smartstudy.paysdk.config.RequestConfig;
import com.smartstudy.paysdk.handler.WeakHandler;
import com.smartstudy.paysdk.util.ConstantUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 网络请求管理工具类
 * Created by louis on 2017/3/2.
 */
public class RequestServer {

    private static RequestServer mInstance;
    private OkHttpClient mOkHttpClient;
    /**
     * 全局处理子线程和M主线程通信
     */
    private WeakHandler okHttpHandler;

    private RequestServer() {
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        //读取超时
        ClientBuilder.readTimeout(ConstantUtils.READ_TIMEOUT, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(ConstantUtils.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(ConstantUtils.WRITE_TIMEOUT, TimeUnit.SECONDS);
        // https支持
        ClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        ClientBuilder.sslSocketFactory(initSSLSocketFactory(), initTrustManager());
        mOkHttpClient = ClientBuilder.build();
        //初始化Handler
        okHttpHandler = new WeakHandler();
    }

    public static RequestServer getInstance() {
        RequestServer inst = mInstance;
        if (mInstance == null) {
            synchronized (RequestServer.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new RequestServer();
                    mInstance = inst;
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    private Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();

        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();

        return headers;
    }

    /**
     * post请求参数
     *
     * @param BodyParams
     * @return
     */
    private static RequestBody setRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                if (BodyParams.get(key) != null) {
                    formEncodingBuilder.add(key, BodyParams.get(key));
                }
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    private String setUrlParams(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams == null) {
            mapParams = new HashMap<>();
        }
//        putGetRequestParams(mapParams);
        Iterator<String> iterator = mapParams.keySet().iterator();
        String key = "";
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            strParams += "&" + key + "=" + mapParams.get(key);
        }
        if (strParams.length() > 0) {
            return "?" + strParams.substring(1);
        }
        return strParams;
    }

    /**
     * get请求
     *
     * @param config
     * @param callback
     */
    public void doGet(RequestConfig config, final Callback callback) {
        final Request.Builder requestBuilder = new Request.Builder();
        //添加URL地址
        requestBuilder.url(config.getUrl() + setUrlParams(config.getParams()));
        requestBuilder.removeHeader("User-Agent");
        //添加请求头
        requestBuilder.headers(setHeaders(getHttpHeaderParams()));
        //添加标签
        requestBuilder.tag(config.getUrl());
        Request request = requestBuilder.build();
        doGetReq(request, callback);
    }

    /**
     * post请求
     *
     * @param config
     * @param callback
     */
    public void doPost(RequestConfig config, final Callback callback) {
        Request.Builder requestBuilder = new Request.Builder();
        //添加URL地址
        requestBuilder.url(config.getUrl());
        requestBuilder.post(setRequestBody(config.getParams()));
        requestBuilder.removeHeader("User-Agent");
        //添加请求头
        requestBuilder.headers(setHeaders(getHttpHeaderParams()));
        //添加标签
        requestBuilder.tag(config.getUrl());
        Request request = requestBuilder.build();
        doRequest(request, callback);
    }

    private void doRequest(Request request, final Callback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack(ConstantUtils.GET_DATA_FAILED, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                doResponse(response, callback);
            }
        });
    }

    private void doGetReq(Request request, final Callback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack(ConstantUtils.GET_DATA_FAILED, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String data = null;
                        if ("gzip".equalsIgnoreCase(response.header("Content-Encoding")) || false) {
                            InputStream inputStream = response.body().byteStream();
                            BufferedInputStream bis = new BufferedInputStream(inputStream);
                            inputStream = new GZIPInputStream(bis);
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(inputStreamReader);
                            StringBuffer sb = new StringBuffer("");
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                                sb.append("\n");
                            }
                            inputStreamReader.close();
                            br.close();
                            data = sb.toString();
                        } else {
                            //没进行压缩
                            data = response.body().string();
                        }
                        if (data != null) {
                            ResponseBean result = JSON.parseObject(data, ResponseBean.class);
                            if ("0".equals(result.getCode())) {
                                successCallBack(result.getData(), callback);
                            } else {
                                String msg = result.getMsg();
                                failedCallBack(TextUtils.isEmpty(msg) ? ConstantUtils.GET_DATA_FAILED : msg, callback);
                            }
                        }
                    }
                    //关闭防止内存泄漏
                    if (response.body() != null) {
                        response.body().close();
                    }
                } catch (IOException e) {
                    failedCallBack(ConstantUtils.GET_DATA_FAILED, callback);
                }
            }
        });
    }

    private void doResponse(Response response, Callback callback) {
        if (response != null) {
            if (response.isSuccessful()) {
                String data = null;
                try {
                    data = response.body().string();
                } catch (IOException e) {
                    failedCallBack(ConstantUtils.GET_DATA_FAILED, callback);
                }
                if (data != null) {
                    ResponseBean result = JSON.parseObject(data, ResponseBean.class);
                    if ("0".equals(result.getCode())) {
                        successCallBack(result.getData(), callback);
                    } else {
                        String msg = result.getMsg();
                        failedCallBack(TextUtils.isEmpty(msg) ? ConstantUtils.GET_DATA_FAILED : msg, callback);
                    }
                }
                //关闭防止内存泄漏
                if (response.body() != null) {
                    response.body().close();
                }
            }
        }
    }

    /**
     * 请求参数中加入公共header参数
     *
     * @return
     */
    private Map<String, String> getHttpHeaderParams() {
        Map<String, String> params = new HashMap<>();
        return params;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result, final Callback<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final String errorMsg, final Callback<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onErr(errorMsg);
                }
            }
        });
    }

    private SSLSocketFactory initSSLSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            X509TrustManager[] xTrustArray = new X509TrustManager[]
                    {initTrustManager()};
            sslContext.init(null,
                    xTrustArray, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    private X509TrustManager initTrustManager() {
        X509TrustManager mTrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        return mTrustManager;
    }
}
