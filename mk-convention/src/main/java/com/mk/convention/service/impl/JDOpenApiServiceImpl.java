package com.mk.convention.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.service.HttpService;
import com.mk.convention.service.JDOpenApiService;
import com.mk.convention.utils.JDOpenApiUtils;
import com.stylefeng.guns.core.util.ResponseCode;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class JDOpenApiServiceImpl implements JDOpenApiService{
    private Logger logger = LoggerFactory.getLogger(JDOpenApiServiceImpl.class);

    @Value("${jd.openApi.host}")
    private String jdOpenApiHost;

    @Value("${jd.openApi.path}")
    private String path;

    @Value("${grantType}")
    private String grantType;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${jd.userName}")
    private String userName;

    @Value("${jd.passWord}")
    private String passWord;

    private final String scope ="" ;


    @Value("${jd.getAccessToken}")
    private String getAccessToken;

    //从京东获取的token 有效期24小时
    private  final static String ACCESS_TOKEN="nTj8IDer55E5JOZ5MmycfizTj";

    //獲取商品所有商品類目信息
    @Value("${jd.openApi.getPageNum}")
    private String getPageNum;

    //获取池内商品编号
    @Value("${jd.openApi.getSkuByPage}")
    private String getSkuByPage ;

    //获取商品详情信息
    @Value("${jd.openApi.getDetail}")
    private  String getDetail;

    //商品上下架狀態
    @Value("${jd.openApi.getSkuState}")
    private String getSkuState;


    @Autowired
    private HttpService commonHttpService;

    @Override
    public JsonResult getAccessToken() {
        HashMap<String, String> data = new HashMap<>();
        return handle(getAccessToken,data,".jd.getAccessToken");
    }

    /**
     * JD获取所有商品池编号接口
     * @Author liukun
     * @param
     * @return
     */
    public JsonResult getPageNum(){
        HashMap<String, String> data = new HashMap<>();
        data.put("token",ACCESS_TOKEN);
        return packageParams(getPageNum,data,"jd.getPageNum");
    }

    @Override
    public JsonResult getSkuByPage(String PageNum, Integer pageNo) {
        HashMap<String,String> data = new HashMap();
        data.put("token",ACCESS_TOKEN);
        data.put("pageNum",PageNum);
        data.put("pageNo",pageNo.toString());
        return packageParams(getSkuByPage,data,"jd.getSkuByPage");
    }

    @Override
    public JsonResult getDetail(String skuId, HashMap<String, String> Paramsdata) {
        HashMap<String, String> data = new HashMap<>();
        data.put("token",ACCESS_TOKEN);
        data.put("sku",skuId);
        return packageParams(getDetail,data,"jd.getDetail");
    }

    @Override
    public JsonResult getSkuSate(String skuIds) {
        HashMap<String, String> data = new HashMap<>();
        data.put("token",ACCESS_TOKEN);
        data.put("sku",skuIds);
        return packageParams(getSkuState,data,"jd.getSkuState");
    }

    /**
     * 组装参数通过httpclient发起请求访问京东开发api接口
     * @Author liukun
     * @param path,data,method,logTag
     * @return
     */
    private JsonResult handle(String path, HashMap<String, String> data, String logTag) {

        TreeMap<String, String> paramsMap = null;

        paramsMap = JDOpenApiUtils.packageParams(grantType,clientId,clientSecret,userName,passWord, scope,data);

        // 组装参数
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            logger.error("\r\n\n ArgName: "+ entry.getKey() + " argValue: " + entry.getValue() );
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String url = jdOpenApiHost + path;
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
     * @Author liukun
     * @param
     * @return
     */
    private JsonResult packageParams(String method,HashMap<String, String> data, String logTag){

        TreeMap<String, String> paramsMap = null;
        paramsMap = JDOpenApiUtils.packageParams(data);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String url = jdOpenApiHost + method;
        System.out.println("京东API请求："+url);
        try {
            long t1 = System.currentTimeMillis();

            // 调用京东open api
            String response = commonHttpService.doPost(url, params, "utf-8");
//            OKHttpClientUtil.httpPost(url, paramsMap);

            logger.info(""+response+"");
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

    private JsonResult handleResponse(String response) {

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

    private JsonResult initServerError(String msg) {
        return initFailureResult(ResponseCode.SERVER_ERROR, msg);
    }

    private JsonResult initFailureResult(int code, String msg) {
        JsonResult JsonResult = new JsonResult(code);
        JsonResult.setMessage(msg);
        return JsonResult;
    }

    private JsonResult initSuccessResult(Object result) {
        JsonResult JsonResult = new JsonResult(ResponseCode.SUCCESS);
        if (result != null)
            JsonResult.setResult(result);
        return JsonResult;
    }
}
