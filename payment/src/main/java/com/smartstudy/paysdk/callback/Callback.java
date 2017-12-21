package com.smartstudy.paysdk.callback;

/**
 * 网络请求基本回调
 * Created by louis on 2017/3/6.
 */
public interface Callback<T> {

    void onErr(String msg);

    void onSuccess(T result);

}
