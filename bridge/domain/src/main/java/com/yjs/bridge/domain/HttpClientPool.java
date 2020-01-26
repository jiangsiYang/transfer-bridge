package com.yjs.bridge.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpClientPool implements IHttpTransfer {
    /**
     * 创建HTTPS 客户端
     *
     * @return 单例模式的客户端
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static volatile HttpClient httpClient = null;


    /**
     * 生成单例HttpClient对象
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static HttpClient getHttpsClient() throws NoSuchAlgorithmException, KeyManagementException {
        if (httpClient != null) {
            return httpClient;
        }
        synchronized (HttpClientPool.class) {
            if (httpClient != null) {
                return httpClient;
            }
            X509TrustManager xtm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{xtm}, null);
            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", scsf).build();
            PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(sfr);
            pcm.setMaxTotal(300);
            pcm.setDefaultMaxPerRoute(30);
            httpClient = HttpClientBuilder.create().setConnectionManager(pcm).build();
            return httpClient;
        }
    }


    public Pair<Integer, HttpEntity> post(HttpPostParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        //请求单例HttpClient
        HttpClient client = getHttpsClient();
        //初始化HttpPost对象
        HttpPost request = new HttpPost(httpParam.getUrl());
        //设置超时时间
        request.setConfig(getRequestConfig(httpParam));

        //传输格式采用json格式
        request.setHeader("Content-Type", httpParam.getDataFormat());
        //设置body信息
        StringEntity postParams = new StringEntity(new Gson().toJson(httpParam.getBodyParams()), "utf-8");
        request.setEntity(postParams);

        //设置header
        if (httpParam.getHeaders() != null) {
            for (Map.Entry<String, String> e : httpParam.getHeaders().entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        }

        HttpResponse response = client.execute(request);
        try {
            return Pair.of(response.getStatusLine().getStatusCode(), response.getEntity());
        } finally {
            //最后，切记一定要把connect的内容使用完，否则这个connect还有内容在是不能被复用的，会导致connect满了无限阻塞
            EntityUtils.consumeQuietly(response.getEntity());
        }
    }

    /**
     * 最底层的get请求，业务方法不能直接调用，应该再加个接口，负责处理连接池的释放等问题（怎么强制执行，以防遗漏这是个问题）
     *
     * @param httpParam
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public HttpResponse get(HttpGetParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        //请求单例HttpClient
        HttpClient client = getHttpsClient();
        //发送get请求
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

        if (httpParam.getUrlParams() != null && httpParam.getUrlParams().size() > 0) {
            for (Map.Entry<String, String> entry : httpParam.getUrlParams().entrySet()) {
                urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        String paramUrl = URLEncodedUtils.format(urlParameters, Charset.forName("UTF-8"));
        HttpGet request = new HttpGet(httpParam.getUrl() + "?" + paramUrl);
        //设置超时时间
        request.setConfig(getRequestConfig(httpParam));
        return client.execute(request);
    }

    private static RequestConfig getRequestConfig(HttpParam httpParam) {
        return RequestConfig.custom().setConnectionRequestTimeout(httpParam.getConnectionRequestTimeout()).setConnectTimeout(httpParam.getConnectTimeout()).setSocketTimeout(httpParam.getSocketTimeout()).build();
    }

    public Pair<Integer, BufferedImage> downloadImage(HttpGetParam httpParam) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpResponse response = get(httpParam);
        try {
            if (response.getHeaders("Content-Type")[0].toString().contains("image")) {
                InputStream inputStream = response.getEntity().getContent();

                BufferedImage bi = ImageIO.read(inputStream);

                return Pair.of(response.getStatusLine().getStatusCode(), bi);

            }
        } finally {
            //最后，切记一定要把connect的内容使用完，否则这个connect还有内容在是不能被复用的，会导致connect满了无限阻塞
            EntityUtils.consumeQuietly(response.getEntity());
        }
        return null;
    }
}
