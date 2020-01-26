package com.yjs.bridge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yjs.bridge.domain.HttpClientPool;
import com.yjs.bridge.domain.HttpGetParam;
import com.yjs.bridge.domain.HttpParam;
import com.yjs.bridge.domain.HttpPostParam;
import com.yjs.bridge.service.ITransferService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransferService implements ITransferService {
    @Autowired
    private HttpClientPool httpClientPool;

    public BufferedImage transferImage(String url) {

        Map<String, String> paramMap = new HashMap();
        paramMap.put("Referer", "\"\"");
        paramMap.put("User-Agent", "Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");

        HttpGetParam httpGetParam = (HttpGetParam) HttpGetParam.getInstance().setHeaders(paramMap).setUrl(url);

        try {
            Pair<Integer, BufferedImage> result = httpClientPool.downloadImage(httpGetParam);
            if (result.getKey() == 200) {
                return result.getValue();
            }
        } catch (Throwable throwable) {
            System.out.println(ExceptionUtils.getStackTrace(throwable));
        }
        return null;
    }
}
