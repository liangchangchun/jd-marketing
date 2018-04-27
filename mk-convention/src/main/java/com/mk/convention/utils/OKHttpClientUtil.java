package com.mk.convention.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * TODO(这里用一句话描述这个类的作用)
 *
 * @author sq.xiao@lovego.com
 * @projectName lovego-myself
 * @package com.lovego.cloud.task.util
 * @className ${TYPE_NAME}
 * @date 2018/2/7 17:32
 */
public class OKHttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(OKHttpClientUtil.class);

    private static final MediaType MEDIA_TYPE = MediaType.parse("text/html;charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient();

    static {
        //超时设定
        client.newBuilder().connectTimeout(30, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(30, TimeUnit.SECONDS);
    }


    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        // 创建一个请求
        Request request = new Request.Builder().url(url).build();
        try {
            // 返回实体
            Response response = client.newCall(request).execute();
            logger.error("OKHttp3 httpGet() Response: " + response + "\n\n ");
            // 判断是否成功
            if (response.isSuccessful()) {
                /*
                    获取返回的数据，可通过response.body().string()获取，默认返回的是utf-8格式
                    string()适用于获取小数据信息，如果返回的数据超过1M，建议使用stream()获取返回的数据，
                    因为string() 方法会将整个文档加载到内存中。
                 */
                result = response.body().string();
                logger.error("OKHTTP3 Success httpGet() response.body().string(): " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 异步Get请求
     */
    public static void asyncGet(String url) {
        // 创建一个请求
        Request request = new Request.Builder().url(url).build();
        // 回调
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                logger.error("OKHTTP3 Success asyncGet() response.body().string(): " + response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                logger.error("OKHTTP3 Failure asyncGet() ErrorMsg: " + e.getMessage());
            }
        });
    }


    /**
     * post调用连接
     * @param url 地址
     * @param params 参数集合
     * @methodName httpPost
     * @author xiaosq@lovego.com
     * @date 2018/2/26 15:51
     * @return java.lang.String
     * @throws
     */
    public static String httpPost(String url, Map<String, Object> params) {

        String result = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            /**
             * 遍历key
             */
            if (null != params) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    builder.add(entry.getKey(), entry.getValue().toString());
                }
            }

            RequestBody body = builder.build();

            Request request = new Request.Builder().post(body).url(url).build();

            Response response = client.newCall(request).execute();

            logger.error("OKHttp3 httpPost() Response: " + response + "\n\n ");

            if(response.isSuccessful()) {
                result = response.body().string();
                logger.error("OKHttp3 Success httpPost() Response.body().string(): " + result + "\n\n ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * post调用连接
     * @param url 地址
     * @param params 参数集合
     * @methodName httpPost
     * @author xiaosq@lovego.com
     * @date 2018/2/26 15:51
     * @return java.lang.String
     * @throws
     */
    public static String httpPost(String url, TreeMap<String, String> params) {

        String result = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            /**
             * 遍历key
             */
            if (null != params) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    builder.add(entry.getKey(), entry.getValue().toString());
                }
            }

            RequestBody body = builder.build();

            Request request = new Request.Builder().post(body).url(url).build();

            Response response = client.newCall(request).execute();

            logger.error("OKHttp3 httpPost() Response: " + response + "\n\n ");

            if(response.isSuccessful()) {
                result = response.body().string();
                logger.error("OKHttp3 Success httpPost() Response.body().string(): " + result + "\n\n ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 异步Post提交字符串
     * 使用Post方法发送一串字符串，但不建议发送超过1M的文本信息
     */
    public static void asyncPost(String url, Map<String, Object> params){

        FormBody.Builder builder = new FormBody.Builder();

        /**
         * 遍历key
         */
        if (null != params) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.error("OKHTTP3 Success asyncPost() Request.url: " + call.request().url());
                logger.error("OKHTTP3 Success asyncPost() Response.code: " + response.code());
                logger.error("OKHTTP3 Success asyncPost() Response.body: " + response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //访问失败, 打印访问地址
                Request r = call.request();

                logger.error("OKHTTP3 Failure asyncPost() Request.url: " + r.url());
                logger.error("OKHTTP3 Failure asyncPost() Request.body: " + r.body());
                logger.error("OKHTTP3 Failure asyncPost() ErrorMsg: " + e.getMessage());
            }
        });
    }


    /**
     * 根据请求方式分发调用请求
     * @param requestType
     * @param url
     * @param params
     * @methodName dispatchRequest
     * @author xiaosq@lovego.com
     * @date 2018/2/7 21:34
     * @return java.lang.String
     * @throws
     */
    public static String dispatchRequest(int requestType, String url, Map<String, Object> params){
        if(1 == requestType) {
            return OKHttpClientUtil.httpGet(url);
        } else {
            return OKHttpClientUtil.httpPost(url, params);
        }
    }




    public static void main(String[] args) {
        String url = "http://192.168.3.121:9013/task/test2";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("arg1", "aaa");
        params.put("arg2", "bbb");
        //System.out.println(JsonUtils.toJson(params));

        httpPost(url, params);
    }


}
