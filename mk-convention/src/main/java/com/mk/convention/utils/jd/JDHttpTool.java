package com.mk.convention.utils.jd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mk.convention.config.property.JDApiConfig;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.service.HttpService;
import com.mk.convention.utils.JDOpenApiUtils;
import com.stylefeng.guns.core.util.ResponseCode;

public class JDHttpTool {
	  private Logger logger = LoggerFactory.getLogger(JDHttpTool.class);
	  private HttpService commonHttpService;
	  /**
	   * 京东配置文件
	   */
	 private JDApiConfig config;
	 public static volatile  JDHttpTool instance = null;
	 public static JDHttpTool getInstance() {
		 if ( instance==null ) {
			 synchronized(JDHttpTool.class) {
				 if ( instance==null ) {
					 instance = new JDHttpTool();
			 	}
			 }
		 }
		 return instance;
	 }
	  
	 private JDHttpTool() {
		 
	 }

	/**
     * 组装参数通过httpclient发起请求访问京东开发api接口
     * @Author liukun
     * @param path,data,method,logTag
     * @return
     */
    public JsonResult handle(String path, HashMap<String, String> data, String logTag) {

        TreeMap<String, String> paramsMap = null;
        String grantType = config.getGrantType(); 
        String clientId = config.getClientId();
        String clientSecret = config.getClientSecret();
        String userName = config.getUserName(); 
        String passWord = config.getPassWord(); 
        String scope = config.getScope();
        paramsMap = JDOpenApiUtils.packageParams(grantType,clientId,clientSecret,userName,passWord, scope,data);

        // 组装参数
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            logger.error("\r\n\n ArgName: "+ entry.getKey() + " argValue: " + entry.getValue() );
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        //String url = jdOpenApiHost + path;
        String url = config.getJdOpenApiHost() + path;
        try {
            long t1 = System.currentTimeMillis();

            // 调用京东open api
            String response = commonHttpService.doPost(url, params, "utf-8");

            long t2 = System.currentTimeMillis();

            logger.info("{} method={}, params={}, response={}, 耗时={}",
                    logTag, JSON.toJSONString(params), response, t2 - t1);

            return handleResponse(response);
        } catch (Exception e) {
            logger.error("{} Exception={}, method={}, params={}",
                    logTag, e.getMessage(), JSON.toJSONString(params));
            return initServerError("server error");
        }
    }

    /**
     * @Author lcc
     * @param
     * @return
     */
    public JsonResult packageParams(String method,HashMap<String, String> data, String logTag){

        TreeMap<String, String> paramsMap = null;
        paramsMap = JDOpenApiUtils.packageParams(data);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

       // String url = jdOpenApiHost + method;
        String url = config.getJdOpenApiHost() + method;
//        System.out.println("京东API请求："+url);
        try {
            long t1 = System.currentTimeMillis();

            // 调用京东open api
            String response = commonHttpService.doPost(url, params, "utf-8");
//            OKHttpClientUtil.httpPost(url, paramsMap);

//            logger.info(""+response+"");
            long t2 = System.currentTimeMillis();

//            logger.info("{} method={}, params={}, response={}, 耗时={}",
//                    logTag, JSON.toJSONString(params), response, t2 - t1);

            return handleResponse(response);
        } catch (Exception e) {
//            logger.error("{} Exception={}, method={}, params={}",
//                    logTag, e.getMessage(), JSON.toJSONString(params));
            return initServerError("server error");
        }
    }

    public JsonResult handleResponse(String response) {

        if (StringUtils.isEmpty(response)) {
            if (null != logger) {
                logger.error(".handleResponse() response is empty");
            }
            return initServerError(".handleResponse() response is empty");
        }

        JSONObject responseObj = JSON.parseObject(response);

        String code = responseObj.getString("resultCode");
        String message = responseObj.getString("resultMessage");
        if (code.equals("0000")) {
            return initSuccessResult(responseObj.get("result"));
        } else {
            if (null != logger) {
                logger.error(".handleResponse() fail. response={}", response);
            }
            return initServerError(message);
        }
    }

    public JsonResult initServerError(String msg) {
        return initFailureResult(ResponseCode.SERVER_ERROR, msg);
    }

    public JsonResult initFailureResult(int code, String msg) {
        JsonResult JsonResult = new JsonResult(code);
        JsonResult.setMessage(msg);
        return JsonResult;
    }

    private JsonResult initSuccessResult(Object result) {
        JsonResult JsonResult = new JsonResult(ResponseCode.SUCCESS);
        if (result != null) {
            JsonResult.setResult(result);
        }
        return JsonResult;
    }
    
    public HttpService getCommonHttpService() {
		return commonHttpService;
	}

	public void setCommonHttpService(HttpService commonHttpService) {
		this.commonHttpService = commonHttpService;
	}
	

	public JDApiConfig getConfig() {
		return config;
	}

	public void setConfig(JDApiConfig config) {
		this.config = config;
	}
}
