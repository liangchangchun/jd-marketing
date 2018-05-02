package com.mk.convention.meta;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;

public class JsonResult extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = -62401379125965102L;

    /*
    * 响应状态码，默认ResponseCode.SUCCESS
    * */
    public static final String CODE = "code";

    /*
    * 响应详情说明
    * */
    public static final String MESSAGE = "msg";

    /*
    * 响应详情数据
    * */
    public static final String RESULT = "result";

    /*
    * 默认构造器
    * */
    public JsonResult() {
        setCode(200);
    }

    /*
    * 状态码构造器
    * */
    public JsonResult(int responseCode) {
        setCode(responseCode);
        setMessage("null");
        setResult(null);
    }

    /*
    * 状态码 + 消息详情构造器
    * */
    public JsonResult(int responseCode, String message) {
        setCode(responseCode);
        setMessage(message);
        setResult(null);
    }

    public void setCode(int responseCode) {
        Assert.notNull(responseCode, "responseCode can not be null");
        put(CODE, responseCode);
    }

    public String getCode() {
        return get(CODE).toString();
    }

    public void setMessage(String message) {
        put(MESSAGE, message);
    }

    public String getMessage() {
        return (String) get(MESSAGE);
    }

    public void setResult(Object result) {
        put(RESULT, result);
    }

    public String getResult() {
        return  (String) get(RESULT);

    }
    
    public JSONArray getResult2() {
        return  (JSONArray) get(RESULT);

    }

    public JSONObject getResult3() {
        return  (JSONObject) get(RESULT);

    }
}
