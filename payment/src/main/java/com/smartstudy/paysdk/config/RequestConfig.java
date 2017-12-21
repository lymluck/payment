package com.smartstudy.paysdk.config;

import java.util.Map;

/**
 * 网络请求基本配置
 * Created by louis on 2017/3/6.
 */
public interface RequestConfig<T> {

    String getUrl();

    Map<T, T> getParams();
}
