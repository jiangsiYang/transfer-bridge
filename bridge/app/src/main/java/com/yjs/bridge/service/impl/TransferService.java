package com.yjs.bridge.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yjs.bridge.domain.HttpClientPool;
import com.yjs.bridge.domain.HttpParam;
import com.yjs.bridge.domain.HttpPostParam;
import com.yjs.bridge.service.ITransferService;
import org.apache.commons.lang3.tuple.Pair;
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
        paramMap.put("Referer", "");
        HttpPostParam httpPostParam = (HttpPostParam) HttpPostParam.getInstance().setBodyParams(paramMap).setUrl(url);
        try {
            Pair<Integer, JSONObject> pair = httpClientPool.post(httpPostParam);

            InputStream inputStream = JSON.parseObject(pair.getValue().get("content").toString(), InputStream.class);

            BufferedImage bi = ImageIO.read(inputStream);
            return bi;
        } catch (Throwable throwable) {
            System.out.println(throwable);
        }

        return null;
    }
}
