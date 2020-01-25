package com.yjs.bridge.domain;

import java.util.Map;

public class HttpGetParam extends HttpParam {
    private HttpGetParam() {
    }

    public static HttpGetParam getInstance() {
        return new HttpGetParam();
    }

    /**
     * url 传参参数
     */
    private Map<String, String> urlParams;

    public Map<String, String> getUrlParams() {
        return urlParams;
    }

    public HttpParam setParams(Map<String, String> params) {
        this.urlParams = params;
        return this;
    }
}
