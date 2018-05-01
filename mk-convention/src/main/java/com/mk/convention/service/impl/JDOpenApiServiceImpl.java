package com.mk.convention.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mk.convention.config.DataSource;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.service.HttpService;
import com.mk.convention.service.JDOpenApiService;
import com.mk.convention.utils.CategoryMachine;
import com.mk.convention.utils.JDOpenApiUtils;
import com.mk.convention.utils.OKHttpClientUtil;
import com.mk.convention.utils.ServiceCategory;
import com.stylefeng.guns.core.util.ResponseCode;
//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private  final static String ACCESS_TOKEN="v1wVvMX1KGZxmeepGbTBEZUwC";

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
    //商品价格
    @Value("${jd.openApi.getSellPrice}")
    private String getSellPrice ;

    private static DataSource dataSource = new DataSource();
    //查询一级地址
    @Value("${jd.openApi.getProvince}")
    private String getProvince ;


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
    @Override
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

    @Override
    public JsonResult getSellPrice(String skuIds) {
        JsonResult pageNum = this.getPageNum();
        List<LinkedHashMap<String,Object>> list = (List<LinkedHashMap<String, Object>>) JSONUtils.parse(pageNum.getResult());
        for (LinkedHashMap<String, Object> stringObjectLinkedHashMap : list) {
            String page_num = (String)stringObjectLinkedHashMap.get("page_num");
            for (int i = 1; ; i++) {
                JsonResult skuByPage = this.getSkuByPage(page_num, 10000);
                List<LinkedHashMap<String,Object>> list1 = (List<LinkedHashMap<String, Object>>) JSONUtils.parse(skuByPage.getResult());
                for (LinkedHashMap<String, Object> objectLinkedHashMap : list1) {
//                    (String) objectLinkedHashMap.get("skuId");

                }
            }
        }
        return new JsonResult(00,"成功");

//        return packageParams(getSellPrice,data,"jd.getSellPrice");
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
        if (result != null) {
            JsonResult.setResult(result);
        }
        return JsonResult;
    }
    @Override
    public JsonResult syncCategoryNew() {
        JsonResult pageNums= getPageNum();
        return pageNums;
    }
    

    @Override
    public JsonResult syncCategory() {
        JsonResult j = getPageNum();
        JSONArray jsonArray = j.getResult2();
        for (int i = 0 ;i < jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String page_num = jsonObject.getString("page_num");
            boolean isok = true;
            int index = 1;
            while (isok){
                j = getSkuByPage(page_num,index);
                JSONObject jsonObject1 = j.getResult3();
                if(jsonObject1==null){
                    isok = false;
                    continue;
                }
                JSONArray skuids = (JSONArray)jsonObject1.get("skuIds");
                if(skuids==null){
                    isok = false;
                    continue;
                }
               
                StringBuffer bf = new StringBuffer();
                for(int jj = 0 ;jj < skuids.size();jj++){
                    String skuid = skuids.get(jj).toString();
                    bf.append("(").append(page_num).append(",").append(skuid).append("),");
                }
                String sqls = bf.toString();
                sqls = sqls.substring(0, sqls.length()-1);
                dataSource.executeUpdate("insert into new_category(sku_id,category_id) values "+sqls, null);
                if(skuids.size()>=500){
                    index++;
                }else{
                    isok = false;
                }
            }
        }
        return null;
    }
    
    @Override
    public JsonResult getProductInfo(String skuId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("token",ACCESS_TOKEN);
        data.put("sku",skuId);
        return productParams(getDetail,data,"jd.getDetail");
    }
    
    /** 
     * @Description:
     * @Param:  
     * @return:  
     * @Author: lzm
     * @Date: 2018/4/28 
     */ 
     private JsonResult productParams(String method,HashMap<String, String> data, String logTag){

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
//             OKHttpClientUtil.httpPost(url, paramsMap);

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
     
    
    @Override
    public JsonResult syncCategoryDetail() {
        ArrayList list = dataSource.executeQuery("select sku_id from category where category_id in ('9736','9737','9738','9739','9740','13757','1590','1591','1592','1593')",null);
        List<JSONObject> array = new ArrayList<>();
        for (Object object:list){
            String skuId = JSONObject.toJSONString(object);
            skuId = skuId.replace("[\"","");
            skuId = skuId.replace("\"]","");
            JsonResult jsonResult = getProductInfo(skuId);
            String code = jsonResult.getCode();
            if("200".equals(code)){
                array.add(jsonResult.getResult3());
            }

        }
        String sql = "insert into product_other_info(sku_id,brand_name,name,product_area,upc,sale_unit,category,introduction,param,wareQD,image_path,weight) values";
        String sql2 = "";
        for(int i=0;i<array.size();i++){
            JSONObject jsonObject = array.get(i);
            String[] parameters = null;
            if(array.size()-i<100){
                parameters = new String[12*(array.size()-i)];
                parameters[(i%100)*12 + 0] = jsonObject.getString("sku");
                parameters[(i%100)*12 + 1] = jsonObject.getString("brandName");
                parameters[(i%100)*12 + 2] = jsonObject.getString("name");
                parameters[(i%100)*12 + 3] = jsonObject.getString("productArea");
                parameters[(i%100)*12 + 4] = jsonObject.getString("upc");
                parameters[(i%100)*12 + 5] = jsonObject.getString("saleUnit");
                parameters[(i%100)*12 + 6] = jsonObject.getString("category");
                parameters[(i%100)*12 + 7] = jsonObject.getString("introduction");
                parameters[(i%100)*12 + 8] = jsonObject.getString("param");
                parameters[(i%100)*12 + 9] = jsonObject.getString("wareQD");
                parameters[(i%100)*12 + 10] = jsonObject.getString("imagePath");
                parameters[(i%100)*12 + 11] = jsonObject.getString("weight");
                sql2 += " (?,?,?,?,?,?,?,?,?,?,?,?),";
                if(i!=0 && i%100==0){
                    sql2 = sql2.substring(0,sql.length()-1);
                    dataSource.executeUpdate(sql+sql2,parameters);
                    sql2 = "";
                }
            }else{
                parameters = new String[12*100];
                parameters[(i%100)*12 + 0] = jsonObject.getString("sku");
                parameters[(i%100)*12 + 1] = jsonObject.getString("brandName");
                parameters[(i%100)*12 + 2] = jsonObject.getString("name");
                parameters[(i%100)*12 + 3] = jsonObject.getString("productArea");
                parameters[(i%100)*12 + 4] = jsonObject.getString("upc");
                parameters[(i%100)*12 + 5] = jsonObject.getString("saleUnit");
                parameters[(i%100)*12 + 6] = jsonObject.getString("category");
                parameters[(i%100)*12 + 7] = jsonObject.getString("introduction");
                parameters[(i%100)*12 + 8] = jsonObject.getString("param");
                parameters[(i%100)*12 + 9] = jsonObject.getString("wareQD");
                parameters[(i%100)*12 + 10] = jsonObject.getString("imagePath");
                parameters[(i%100)*12 + 11] = jsonObject.getString("weight");
                sql2 += " (?,?,?,?,?,?,?,?,?,?,?,?),";
                if(i!=0 && i%100==0){
                    sql2 = sql2.substring(0,sql.length()-1);
                    dataSource.executeUpdate(sql+sql2,parameters);
                    sql2 = "";
                }
            }
        }
        return null;
    }
    
    @Override
    public JsonResult syncCategoryDetail2() {
    	
    	
        ArrayList list = dataSource.executeQuery("select sku_id from category where category_id in ('9736','9737','9738','9739','9740','13757','1590','1591','1592','1593')",null);
        ExecutorService threadPool = Executors.newFixedThreadPool(5000);//
       // List<JSONObject> array = new ArrayList<>();
        for (Object object:list){
            String skuId = JSONObject.toJSONString(object);
            skuId = skuId.replace("[\"","");
            skuId = skuId.replace("\"]","");
            final String skuid = skuId ;
            threadPool.execute(new Runnable() {
				@Override
				public void run() {
					JsonResult jsonResult = getProductInfo(skuid);
		            String code = jsonResult.getCode();
		            if("200".equals(code)){
		                //array.add(jsonResult.getResult3());
		                CategoryMachine.getInstance().getCategoryManager().addCategory(jsonResult.getResult3());
		            }
				}});
        }
        
        for(int i=1;i<100;i++){
    		DataSource ds = new DataSource();
			ServiceCategory serviceCategory =  new ServiceCategory(ds);
			serviceCategory.setNumber(i);
			serviceCategory.start();
		}
	
            
        return null;
    }

    /**
     * 获取京东所有的地址并入库
     * @param
     * @methodName saveAllJdArea
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:14
     * @return com.mk.convention.meta.JsonResult
     * @throws
     */
    @Override
    public JsonResult saveAllJdArea() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", ACCESS_TOKEN);

        return null;
    }


    @Override
    public JsonResult getAllFirstJdAreaList() {
        Map<String, Object> params = this.getBaseParam();
        String url = this.jdOpenApiHost + this.getProvince;

        String result = OKHttpClientUtil.httpPost(url, params);

        JSONObject json = JSONObject.parseObject(result);
        if (null != json && "200".equals(json.get("status"))) {
            //物流详细信息
            JSONArray jsonArray = JSONArray.parseArray(json.getString("aaa"));
        } else {
        }
        return null;
    }


    @Override
    public JsonResult getSecondJdAreaList(int firstAreaId) {
        return null;
    }

    @Override
    public JsonResult getThirdJdAreaList(int secondAreaId) {
        return null;
    }

    @Override
    public JsonResult getFourthJdAreaList(int thirdAreaId) {
        return null;
    }


    /**
     * 基础查询参数
     * @methodName baseParam
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:39
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     */
    private Map<String,Object> getBaseParam(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("token", ACCESS_TOKEN);

        return params;
    }
    

}
