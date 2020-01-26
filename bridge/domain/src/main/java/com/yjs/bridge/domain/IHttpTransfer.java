package com.yjs.bridge.domain;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface IHttpTransfer {
    Pair<Integer, HttpEntity> post(HttpPostParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    HttpResponse get(HttpGetParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException;
}
