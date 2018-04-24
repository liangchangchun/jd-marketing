package com.stylefeng.guns.core.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.commons.lang.StringUtils ;


/**
 * Author: lcc
 * Version: 1.1
 * Date: 2018/04/02
 * Description: 封装的http请求类.
 * Copyright (c) 2018 zww365网络. All rights reserved.
 */  
public class HttpClientUtil {
      
    /** 
     * 初始化HttpClient 
     */  
    private CloseableHttpClient httpClient = HttpClients.createDefault();  
    
	private static HttpClientUtil _instance;
	
	public static HttpClientUtil getInstance() {
		if(_instance == null) {
			return (new HttpClientUtil());
		}else {
			return _instance;
		}
	}
    /** 
     * POST方式调用 
     *  
     * @param url 
     * @param params 参数为NameValuePair键值对对象 
     * @return 响应字符串 
     * @throws ClientProtocolException 
     * @throws java.io.UnsupportedEncodingException 
     */  
    public String executeByPOST(String url, List<NameValuePair> params) throws ClientProtocolException {  
        HttpPost post = new HttpPost(url);  
          
        ResponseHandler<String> responseHandler = new BasicResponseHandler();  
        String responseJson = null;  
        try {  
            if (params != null) {  
                post.setEntity(new UrlEncodedFormEntity(params));  
            }  
            responseJson = httpClient.execute(post, responseHandler);
        }  
        catch (ClientProtocolException e) {  
            throw e;
        }  
        catch (IOException e) {  
            e.printStackTrace();  
        }  
        finally {  
            httpClient.getConnectionManager().closeExpiredConnections();  
            httpClient.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);  
        }  
        return responseJson;  
    }  
      
    /** 
     * Get方式请求 
     *  
     * @param url 带参数占位符的URL，例：http://ip/User/user/center.aspx?_action=GetSimpleUserInfo&codes={0}&email={1} 
     * @param params 参数值数组，需要与url中占位符顺序对应 
     * @return 响应字符串 
     * @throws IOException 
     * @throws java.io.UnsupportedEncodingException 
     */  
    public String executeByGET(String url, Object[] params) throws IOException {  
        String messages = MessageFormat.format(url, params);  
        HttpGet get = new HttpGet(messages);  
        ResponseHandler<String> responseHandler = new BasicResponseHandler();  
        String responseJson = null;  
        try {  
            responseJson = httpClient.execute(get, responseHandler);
        }  
        catch (ClientProtocolException e) {  
        	throw e;
        }  
        catch (IOException e) {  
        	throw e; 
        }  
        finally {  
            httpClient.getConnectionManager().closeExpiredConnections();  
            httpClient.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);  
        }  
        return responseJson;  
    }  
      
    /** 
     * @param url 
     * @return 
     * @throws IOException 
     */  
    public String executeByGET(String url) throws IOException {  
        HttpGet get = new HttpGet(url);  
        ResponseHandler<String> responseHandler = new BasicResponseHandler();  
        String responseJson = null;  
        try {  
            responseJson = httpClient.execute(get, responseHandler); 
        }  
        catch (ClientProtocolException e) {  
        	throw e;
        }  
        catch (IOException e) {  
        	throw e; 
        }  
        finally {  
            httpClient.getConnectionManager().closeExpiredConnections();  
            httpClient.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);  
        }  
        return responseJson;  
    }  
  
    /** 
	 * 绕过验证 
	 *   
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 */  
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
	    SSLContext sc = SSLContext.getInstance("SSLv3");  
	  
	    // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override  
	        public void checkClientTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override  
	        public void checkServerTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override  
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	  
	    sc.init(null, new TrustManager[] { trustManager }, null);  
	    return sc;  
	}  
	
	/** 
	 * 模拟请求 
	 *  
	 * @param url       资源地址 
	 * @param map   参数列表 
	 * @param encoding  编码 
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 * @throws IOException  
	 * @throws ClientProtocolException  
	 */  
	public static String sendHttps(String url, Map<String,String> map,String encoding) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {  
	    String body = "";  
	    //采用绕过验证的方式处理https请求  
	    SSLContext sslcontext = createIgnoreVerifySSL();  
	      
	       // 设置协议http和https对应的处理socket链接工厂的对象  
	       Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
	           .register("http", PlainConnectionSocketFactory.INSTANCE)  
	           .register("https", new SSLConnectionSocketFactory(sslcontext))  
	           .build();  
	       PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
	       HttpClients.custom().setConnectionManager(connManager);  
	  
	       //创建自定义的httpclient对象  
	    CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
	//       CloseableHttpClient client = HttpClients.createDefault();  
	      
	    //创建post方式请求对象  
	    HttpPost httpPost = new HttpPost(url);  
	      
	    //装填参数  
	    List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	    if(map!=null){  
	        for (Entry<String, String> entry : map.entrySet()) {  
	            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
	        }  
	    }  
	    //设置参数到请求对象中  
	    httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));  
	  
	    System.out.println("请求地址："+url);  
	    System.out.println("请求参数："+nvps.toString());  
	      
	    //设置header信息  
	    //指定报文头【Content-type】、【User-Agent】  
//	    httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
	    httpPost.setHeader("Content-type", "application/json"); 
	    httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
	      
	    //执行请求操作，并拿到结果（同步阻塞）  
	    CloseableHttpResponse response = client.execute(httpPost);  
	    //获取结果实体  
	    HttpEntity entity = response.getEntity();  
	    if (entity != null) {  
	        //按指定编码转换结果实体为String类型  
	        body = EntityUtils.toString(entity, encoding);  
	    }  
	    EntityUtils.consume(entity);  
	    //释放链接  
	    response.close();  
	       return body;  
	}

	/**
	 * 获取Ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAdrress(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
}  