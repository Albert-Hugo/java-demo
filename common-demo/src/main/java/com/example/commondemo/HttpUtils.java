package com.example.commondemo;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * HTTP工具类
 *
 * @author SUMMER
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);


    private static PoolingHttpClientConnectionManager httpConnectionManager;

    private static ConnectionKeepAliveStrategy keepAliveStrategy;

    private static SSLConnectionSocketFactory sslConnectionSocketFactory;

    static CloseableHttpClient httpClient;
    static {
        httpClient= HttpClients.custom()
                .setConnectionManager(getSingletonConnectionManager())
                .setKeepAliveStrategy(getSingletonConnectionKeepAliveStrategy())
                .build();
    }
    private static HttpClient getHttpClient() {
//        return httpClient;
        return HttpClients.custom()
                .setConnectionManager(getSingletonConnectionManager())
                .setKeepAliveStrategy(getSingletonConnectionKeepAliveStrategy())
                .build();
    }

    private static HttpClient getHttpsClient() {
        try {
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(getSingletonConnectionSocktfactory())
                    .setConnectionManager(getSingletonConnectionManager()).build();
            return httpClient;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    private static synchronized SSLConnectionSocketFactory getSingletonConnectionSocktfactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (sslConnectionSocketFactory == null) {
            TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);
        }
        return sslConnectionSocketFactory;
    }

    private static PoolingHttpClientConnectionManager connectionManager() {
        try {
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("https", getSingletonConnectionSocktfactory())
                            .register("http", new PlainConnectionSocketFactory())
                            .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 设置最大线程数为50
            cm.setMaxTotal(50);
            // 同网址最高线程数40
            cm.setDefaultMaxPerRoute(40);
            return cm;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取单例的ConnectionManager
     *
     * @return
     */
    private static synchronized PoolingHttpClientConnectionManager getSingletonConnectionManager() {
        if (httpConnectionManager == null) {
            httpConnectionManager = connectionManager();
        }
        return httpConnectionManager;
    }

    /**
     * keepAlive策略
     *
     * @return
     */
    private static synchronized ConnectionKeepAliveStrategy getSingletonConnectionKeepAliveStrategy() {
        if (keepAliveStrategy == null) {
            keepAliveStrategy = new ConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    HeaderElementIterator it = new BasicHeaderElementIterator
                            (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                    while (it.hasNext()) {
                        HeaderElement he = it.nextElement();
                        String param = he.getName();
                        String value = he.getValue();
                        if (value != null && param.equalsIgnoreCase
                                ("timeout")) {
                            return Long.parseLong(value) * 1000;
                        }
                    }
                    return 30 * 1000L;//如果没有约定，则默认定义时长为30s
                }
            };
        }
        return keepAliveStrategy;
    }

    /**
     * HTTPGET
     *
     * @param url
     * @param paramsMap
     * @param encoding
     * @return
     */
    public final static String httpGet(String url,
                                       Map<String, String> paramsMap, String encoding) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=" + encoding);
        return get(url, paramsMap, headerMap, encoding, url != null && url.startsWith("https:") ? "https" : "http");
    }

    /**
     * HTTPSGET
     *
     * @param url
     * @param paramsMap
     * @param encoding
     * @return
     */
    public final static String httpsGet(String url,
                                        Map<String, String> paramsMap, String encoding) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=" + encoding);
        return get(url, paramsMap, headerMap, encoding, url != null && url.startsWith("https:") ? "https" : "http");
    }

    /**
     * HTTPSPOST
     *
     * @param url
     * @param paramsMap
     * @param encoding
     * @return
     */
    public final static String httpPost(String url,
                                        Map<String, String> paramsMap, String encoding) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=" + encoding);
        //组装参数
        List<BasicNameValuePair> params = parseMap2BasicForm(paramsMap);
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("httpPost fail" + e.getMessage(), e);
        }
        return post(url, headerMap, entity, encoding, url != null && url.startsWith("https:") ? "https" : "http");
    }

    /**
     * HTTPSPOST
     *
     * @param url
     * @param paramsMap
     * @param encoding
     * @return
     */
    public final static String httpPost(String url, Map<String, String> headerMap,
                                        Map<String, String> paramsMap, String encoding) {
		/*Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=" + encoding);*/
        //组装参数
        List<BasicNameValuePair> params = parseMap2BasicForm(paramsMap);
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(params, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("httpPost fail" + e.getMessage(), e);
        }
        return post(url, headerMap, entity, encoding, url != null && url.startsWith("https:") ? "https" : "http");
    }

    /**
     * HTTPSPOST
     *
     * @param url
     * @param paramsMap
     * @param encoding
     * @return
     */
    public final static String httpsPost(String url,
                                         Map<String, String> paramsMap, String encoding) {
        return httpPost(url, paramsMap, encoding);
    }

    /**
     * HTTPSPOST JSON 改为根据URL的前面几个字母（协议），来进行http或https调用。
     *
     * @param url
     * @param paramJson
     * @param encoding
     * @return
     */
    public final static String httpsPostJson(String url,
                                             String paramJson, String encoding) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json; charset=" + encoding);
        //组装参数
        StringEntity entity = null;
        try {
            entity = new StringEntity(paramJson, encoding);
        } catch (Exception e) {
            logger.error("组装参数失败" + e.getMessage(), e);
        }
        return post(url, headerMap, entity, encoding, url != null && url.startsWith("https:") ? "https" : "http");
    }


    /**
     * HTTP POST XML
     *
     * @param url
     * @param encoding
     * @return
     */
    public final static String httpPostXml(String url, String requestXML, String encoding, String soapAction) throws Exception {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "text/xml; charset=" + encoding);
        headerMap.put("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
        //组装参数
        StringEntity entity = null;
        try {
            entity = new StringEntity(requestXML, encoding);
        } catch (Exception e) {
            logger.error("httpPostXml error : ", e);
        }
        return post(url, headerMap, entity, encoding, url != null && url.startsWith("https:") ? "https" : "http");
    }

    /**
     * GET
     *
     * @param url
     * @param paramsMap
     * @param headerMap
     * @param encoding
     * @param type      http or https
     * @return
     */
    private final static String get(String url, Map<String, String> paramsMap,
                                    Map<String, String> headerMap, String encoding, String type) {
        String result = "";
        // 组装参数
        String paramStr = "";
        Set<Entry<String, String>> paramEntries = paramsMap.entrySet();
        for (Entry<String, String> entry : paramEntries) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            paramStr += paramStr = "&" + key + "=" + val;
        }
        if (!paramStr.equals("")) {
            paramStr = paramStr.replaceFirst("&", "?");
            url += paramStr;
        }
        // 创建一个httpGet请求
        HttpGet request = new HttpGet(url);
        // 组装header参数
        Set<Entry<String, String>> headerEntries = headerMap.entrySet();
        for (Entry<String, String> entry : headerEntries) {
            request.setHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        try {
            // 创建一个htt客户端
            HttpClient httpClient = "https".equals(type) ? getHttpsClient()
                    : getHttpClient();
            // 接受客户端发回的响应
            HttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 得到客户段响应的实体内容
                result = EntityUtils.toString(httpResponse.getEntity(),
                        encoding);
            } else {
                logger.error("URL:" + url + "\tStatusCode:" + statusCode);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * POST
     *
     * @param url
     * @param headerMap
     * @param encoding
     * @param type      http or https
     * @return
     */
    private final static String post(String url,
                                     Map<String, String> headerMap, HttpEntity requestEntity, String encoding, String type) {
        String result = "";

        // 创建一个httpGet请求
        HttpPost request = null;
        // 创建一个htt客户端
        HttpClient httpClient = null;
        try {
            // 创建一个httpGet请求
            request = new HttpPost(url);

            // 组装header参数
            Set<Entry<String, String>> headerEntries = headerMap.entrySet();
            for (Entry<String, String> entry : headerEntries) {
                request.setHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            // 设置参数
            request.setEntity(requestEntity);

            logger.info("Post Data to [" + url + "] ");

            // 创建一个htt客户端
            httpClient = "https".equals(type) ? getHttpsClient()
                    : getHttpClient();
            RequestConfig requestConfig = RequestConfig.custom().
                    setSocketTimeout(30000).setConnectTimeout(30000).build();
            request.setConfig(requestConfig);

            // 接受客户端发回的响应
            HttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 得到客户段响应的实体内容
                result = EntityUtils.toString(httpResponse.getEntity(),
                        encoding);
            }
        } catch (Exception e) {
            if (request == null) {
                throw new RuntimeException("request 不能为空", e);
            }
            request.abort();
            String errorInfo = String.format("httpclient post failed:\nUrl=%s\nError=%s",
                    url,
                    e.toString());
            logger.error(errorInfo, e);
        } finally {
        }

        return result;
    }

    /**
     * 使用此方法 必须要在外面关闭流
     *
     * @param url
     * @param paramJson
     * @param encodin
     * @param type
     * @return
     */
    public final static HttpResponse post(String url,
                                          String paramJson, String encodin, String type) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json; charset=" + encodin);
        //组装参数
        StringEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(paramJson, encodin);
        } catch (Exception e) {
            logger.error("组装参数失败" + e.getMessage(), e);
        }
        logger.info(paramJson);
        HttpResponse result = null;

        // 创建一个httpGet请求
        HttpPost request = null;
        // 创建一个htt客户端
        HttpClient httpClient = null;
        try {
            // 创建一个httpGet请求
            request = new HttpPost(url);

            // 组装header参数
            Set<Entry<String, String>> headerEntries = headerMap.entrySet();
            for (Entry<String, String> entry : headerEntries) {
                request.setHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            // 设置参数
            request.setEntity(requestEntity);

            logger.info("Post Data to [" + url + "] ");

            // 创建一个htt客户端
            httpClient = "https".equals(type) ? getHttpsClient()
                    : getHttpClient();
            RequestConfig requestConfig = RequestConfig.custom().
                    setSocketTimeout(3000).setConnectTimeout(3000).build();
            request.setConfig(requestConfig);

            // 接受客户端发回的响应
            HttpResponse httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 得到客户段响应的实体内容
                result = httpResponse;
            }
        } catch (Exception e) {
            if (request == null) {
                throw new RuntimeException("request 不能为空", e);
            }
            request.abort();
            String errorInfo = String.format("httpclient post failed:\nUrl=%s\nError=%s",
                    url,
                    e.toString());
            logger.error(errorInfo, e);
        } finally {
        }

        return result;
    }

    /**
     * 封装MAP格式的参数到BasicNameValuePair中
     *
     * @param paramsMap
     * @return
     */
    private static final List<BasicNameValuePair> parseMap2BasicForm(
            Map<String, String> paramsMap) {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        ;
        if (paramsMap != null && paramsMap.size() > 0) {
            Iterator<String> it = paramsMap.keySet().iterator();

            String keyTmp = null;
            while (it.hasNext()) {
                keyTmp = it.next();
                params
                        .add(new BasicNameValuePair(keyTmp, paramsMap
                                .get(keyTmp)));
            }
        }
        return params;
    }

    /**
     * http get string from remote machine
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String getNetString(String url) throws IOException,
            ClientProtocolException {
        String result = "";
        HttpClient httpclient = getHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse httpResponse = httpclient.execute(get);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            // 得到客户段响应的实体内容
            result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        }
        get.abort();
        return result;
    }
}
