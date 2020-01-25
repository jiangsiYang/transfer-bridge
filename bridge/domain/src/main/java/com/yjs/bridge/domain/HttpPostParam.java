package com.yjs.bridge.domain;

import java.util.Map;

public class HttpPostParam extends HttpParam {
    private HttpPostParam() {
    }

    public static HttpPostParam getInstance() {
        return new HttpPostParam();
    }

    /**
     * 传输内容格式
     */
    private String dataFormat = "application/json";

    /**
     * body 传参参数
     */
    private Map<String, String> bodyParams;

    public Map<String, String> getBodyParams() {
        return bodyParams;
    }

    public HttpPostParam setBodyParams(Map<String, String> bodyParams) {
        this.bodyParams = bodyParams;
        return this;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public HttpParam setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
        return this;
    }
}
