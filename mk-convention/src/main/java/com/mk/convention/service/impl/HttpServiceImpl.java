package com.mk.convention.service.impl;
import com.alibaba.fastjson.JSON;
import com.mk.convention.service.HttpService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service("commonHttpService")
public class HttpServiceImpl implements HttpService {


    Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);

    @Value("${http.timeout}")
    private int timeout;

    @Value("${http.defaultCharset}")
    private String defaultCharset;

    @Value("${http.connection.maxTotal}")
    private int maxTotal;

    @Value("${http.connection.defaultMaxPerRoute}")
    private int defaultMaxPerRoute;

    private HttpClient httpClient;



    @PostConstruct
    public void init() {
        PoolingHttpClientConnectionManager connectionManager
                = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager).build();
    }

    @Override
    public String doPost(String url, List<NameValuePair> parameters, String charset)
            throws IOException {
        charset = getCharset(charset);
        HttpPost postMethod = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).setRedirectsEnabled(false).build();
        postMethod.setConfig(requestConfig);
        if (parameters != null && parameters.size() > 0) {
            UrlEncodedFormEntity data = new UrlEncodedFormEntity(parameters,
                    charset);
            postMethod.setEntity(data);
        }
        if (logger.isDebugEnabled()) {
            logger.info(
                    "[HttpClientTemplate#doPost] request to url={}, param={}",
                    url, JSON.toJSON(parameters));
        }
        String result = requestAndParse(postMethod, charset);
        if (logger.isDebugEnabled()) {
            logger.info(
                    "[HttpClientTemplate#doPost] response from url={}, param={}, data={}",
                    url, JSON.toJSON(parameters), result);
        }
        return result;
    }

    @Override
    public String doGet(String url, List<NameValuePair> parameters, String charset)
            throws IOException {
        charset = getCharset(charset);
        String queryString = null;
        if (parameters != null && parameters.size() > 0) {
            queryString = URLEncodedUtils.format(parameters, charset);
        }
        String requestUrl = url;
        if (queryString != null) {
            if (!url.contains("?"))
                requestUrl += "?" + queryString;
            else
                requestUrl += "&" + queryString;
        }
        HttpGet getMethod = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).setRedirectsEnabled(false).build();
        getMethod.setConfig(requestConfig);
        if (logger.isDebugEnabled()) {
            logger.debug("[HttpClientTemplate#doGet] request to url={}",
                    requestUrl);
        }
        String result = requestAndParse(getMethod, charset);
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[HttpClientTemplate#doGet] response from url={}, param={}, data={}",
                    url, JSON.toJSON(parameters), result);
        }
        return result;
    }

    private String getCharset(String charset) {
        if (StringUtils.isEmpty(charset)) {
            return defaultCharset;
        }
        return charset;
    }

    private String requestAndParse(HttpUriRequest httpRequest, String charset)
            throws IOException {
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        StatusLine statusLine = httpResponse.getStatusLine();
        if (null == statusLine) {
            throw new IOException("status not specified");
        }
        int statusCode = statusLine.getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            EntityUtils.consumeQuietly(httpResponse.getEntity());
            throw new IOException("status code: " + statusCode);
        }
        HttpEntity entity = httpResponse.getEntity();
        if (null == entity) {
            return null;
        }
        return EntityUtils.toString(entity, charset);
    }
}
