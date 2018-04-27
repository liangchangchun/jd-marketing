package com.mk.convention.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stylefeng.guns.core.util.DateUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class JDOpenApiUtils {

private static Logger logger = LoggerFactory.getLogger(JDOpenApiUtils.class);

    /**
     * 组装参数，包括sign值的生成
     * @return treeMap
     */
    public static TreeMap<String, String> packageParams(String grantType, String clientId,
                                                        String clientSecret, String userName, String passWord, String scope,
                                                        Map<String, String> paramMap) {

        logger.info("\r\n\n clientSecret: " + clientSecret + " clientId: " + clientId + " userName: " + userName + " passWord: " + passWord + " grantType: " + grantType + " scope: " + scope + "\r\n");

        // 构建时间戳
        String timestamp = DateUtils.parseLongToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");

        String md5PassWord=MD5Util.parseStrToMd5L32(passWord);

        //将请求参数按名称排序
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("grant_type", grantType);
        treeMap.put("client_id", clientId);
        treeMap.put("client_secret", clientSecret);
        treeMap.put("timestamp", timestamp);
        treeMap.put("username", userName);
        treeMap.put("password", md5PassWord);
    //  treeMap.put("method", method);

    //    treeMap.put("scope", appKey);  申请权限。（目前推荐为空。为以后扩展用）
        if (null != paramMap) {
            treeMap.putAll(paramMap);
        }

    //    遍历treeMap，将参数值进行拼接
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = treeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            sb.append(key).append("=");
            sb.append(treeMap.get(key));
        }

        //参数值拼接的字符串收尾添加appSecret值
        String waitSignStr =
                clientSecret
                        +timestamp
                        +clientId
                        +userName
                        +md5PassWord
                        +grantType
                        +clientSecret;

        logger.info("\r\n\n waitSignStr: " + waitSignStr + "\r\n\n");

        //获取MD5加密后的字符串
        String sign = MD5Util.parseStrToMd5U32(waitSignStr);

        if (logger.isInfoEnabled()) {
            logger.info(".getSign() param={} sign={}", treeMap, sign + "\r\n\n");
        }

        treeMap.put("sign", sign);
        return treeMap;
    }

    public static TreeMap<String, String> packageParams(
                                                        Map<String, String> paramMap) {

        TreeMap<String, String> treeMap = new TreeMap<>();
        if (null != paramMap) {
            treeMap.putAll(paramMap);
        }
        return treeMap;
    }

}
