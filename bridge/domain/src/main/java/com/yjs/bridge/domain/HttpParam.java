package com.yjs.bridge.domain;

import java.util.Map;

public class HttpParam {

    private String url;


    /**
     * headers参数表
     */
    private Map<String, String> headers;

    /**
     * 3个超时时间
     */
    private int connectionRequestTimeout = 500;
    private int connectTimeout = 500;
    private int socketTimeout = 500;

    public String getUrl() {
        return url;
    }

    public HttpParam setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpParam setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public HttpParam setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public HttpParam setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public HttpParam setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

}
