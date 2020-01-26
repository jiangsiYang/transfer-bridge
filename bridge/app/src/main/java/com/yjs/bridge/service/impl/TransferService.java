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
//        paramMap.put("Referer", "https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247484235&idx=1&sn=4c3b6d13335245d4de1864672ea96256&chksm=ebd7424adca0cb5cb26eb51bca6542ab816388cf245d071b74891dd3f598ccd825f8611ca20c&token=1834317504&lang=zh_CN&scene=21");
        paramMap.put("Referer", "\"\"");
        paramMap.put("User-Agent", "Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14");
        HttpPostParam httpPostParam = (HttpPostParam) HttpPostParam.getInstance().setDataFormat("image/*").setHeaders(paramMap).setUrl("https://img11.360buyimg.com/n1/s450x450_jfs/t1/58799/9/8892/123407/5d664945E8b3a9806/90035fe3c37e06c8.jpg");


        HttpGetParam httpGetParam = (HttpGetParam) HttpGetParam.getInstance().setHeaders(paramMap).setUrl("https://img11.360buyimg.com/n1/s450x450_jfs/t1/58799/9/8892/123407/5d664945E8b3a9806/90035fe3c37e06c8.jpg");

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
