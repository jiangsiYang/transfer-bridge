package com.yjs.bridge.domain;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface IHttpTransfer {
    Pair<Integer, JSONObject> post(HttpPostParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException;

    Pair<Integer, JSONObject> get(HttpGetParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException;
}
