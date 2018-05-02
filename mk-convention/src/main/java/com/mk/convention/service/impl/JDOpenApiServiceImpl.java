package com.mk.convention.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.mk.convention.dao.ProductPriceInfoMapper;
import com.mk.convention.config.DataSource;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.model.Category;
import com.mk.convention.model.ProductPriceInfo;
import com.mk.convention.model.SkuByPage;
import com.mk.convention.respository.OrderRepository;
import com.mk.convention.respository.ProductPriceInfoRepository;
import com.mk.convention.persistence.model.JDBaseArea;
import com.mk.convention.respository.JDBaseAreaRepository;
import com.mk.convention.service.HttpService;
import com.mk.convention.service.JDOpenApiService;
import com.mk.convention.utils.JDOpenApiUtils;
import com.mk.convention.utils.JsonUtils;
import com.mk.convention.utils.OKHttpClientUtil;
import com.mk.convention.utils.jd.JdDataPublisher;
import com.mk.convention.utils.jd.JdTransformTool;
import com.mk.convention.utils.jd.NewCategoryData;
import com.mk.convention.utils.jd.NewCategoryDataReq;
import com.mk.convention.utils.traditional.ProductManager;
import com.stylefeng.guns.core.util.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class JDOpenApiServiceImpl implements JDOpenApiService {

    private Logger logger = LoggerFactory.getLogger(JDOpenApiServiceImpl.class);

    /**
     * 直辖市
     */
    private static final List<String> CharteredCitiesList = Arrays.asList("北京", "天津", "上海", "重庆");


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
    @Value("${jd.AccessToken}")
    private   String ACCESS_TOKEN;

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

    private DataSource dataSource = new DataSource();
    //查询一级地址
    @Value("${jd.openApi.getProvince}")
    private String getProvince ;

    @Value("${jd.openApi.getCity}")
    private String getCity ;

    @Value("${jd.openApi.getCounty}")
    private String getCounty ;

    @Value("${jd.openApi.getTown}")
    private String getTown ;

    @Autowired
    private JDBaseAreaRepository jdBaseAreaRepository;
    //获取商品分类信息
    @Value("${jd.openApi.getCategory}")
    private String getCategory;

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


    @Autowired
    ProductPriceInfoRepository productPriceInfoRepository;
    @Override
    public JsonResult getSellPrice(String skuIds) {
        Long count=0L;

        JsonResult pageNum = this.getPageNum();

        List<Category> list = JsonUtils.fromJson(JsonUtils.toJson(pageNum.getResult4()), List.class, Category.class);
//        ProductPriceInfo productPriceInfo=new ProductPriceInfo();
        List<ProductPriceInfo> productPriceInfos=new ArrayList<>();
        for (Category category : list) {
            for (int i= 1;; i++) {
                JsonResult skuByPage = this.getSkuByPage(category.getPage_num(), i);
                Object result2 = skuByPage.getResult4();
                if (result2==null){
                    break;
                }
                SkuByPage skuByPage1 = JsonUtils.fromJson(JsonUtils.toJson(skuByPage.getResult4()), SkuByPage.class);
                List<Long> skuIds1 = skuByPage1.getSkuIds();
                int i1=0;
                String skuids="";
                for (Long aLong : skuIds1) {
                    skuids+=aLong+",";
                    i1++;
                    if (i1==100){

                        HashMap<String, String> data = new HashMap<>();
                        data.put("token",ACCESS_TOKEN);
                        data.put("sku",skuids.substring(0,skuids.length()-1));
                        data.put("queryExts","containsTax,marketPrice");
                        JsonResult jsonResult = packageParams(getSellPrice, data, "jd.getSellPrice");
                        Object result1 = jsonResult.getResult4();
                        if (result1==null){
                          continue;
                        }
                        List<ProductPriceInfo> list1 = JsonUtils.fromJson(JsonUtils.toJson(result1), List.class, ProductPriceInfo.class);
                        if (list1==null||list1.size()==0){
                            continue;
                        }
                        for (ProductPriceInfo productPriceInfo1 : list1) {
                            count++;
                            productPriceInfo1.setId(count);
                        }

                        productPriceInfos.addAll(list1);

                        i1=0;
                        skuids="";
                    }
                }
            }
            productPriceInfoRepository.save(productPriceInfos);

            System.out.println(productPriceInfos.size());
            productPriceInfos.clear();

        }

        return new JsonResult(00,"成功");

    }

    @Override
    public JsonResult getCategory(Long id) {
        HashMap<String,String> data = new HashMap<>();
        data.put("cid",id.toString());
        data.put("token",ACCESS_TOKEN);
        return packageParams(getCategory,data,"jd.getCategory");
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
                    bf.append("(").append(skuid).append(",").append(page_num).append("),");
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
    public JSONArray syncCategoryNew() {
    	Stopwatch stopwatch = null;
    	JSONObject jsonObject = null;
        JsonResult j = getPageNum();
        Stopwatch stopwatchAll =Stopwatch.createStarted();
        JSONArray jsonArray = j.getResult2();
        long allNum = 0L;
        String name = "";
        String page_num = "";
        int skuNum =0;
        for (int i = 0,length =  jsonArray.size();i < length;i++){
       // int i= 0;
        	stopwatch =Stopwatch.createStarted();
             jsonObject = jsonArray.getJSONObject(i);
             name = jsonObject.getString("name");
             page_num = jsonObject.getString("page_num");
             skuNum = syncCategoryPage(page_num);//skuId数量
            allNum += skuNum;
            long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
            System.out.println("商品池请求编号:【"+page_num+"】,第"+i+"个商品名称:" + name+" 数量:"+skuNum+"程序运行时间: "+nanos+"ns");
        }
        long nanosAll = stopwatchAll.elapsed(TimeUnit.NANOSECONDS);
        long nanosAlls = stopwatchAll.elapsed(TimeUnit.SECONDS);
        System.out.println("商品池请求编号全部获取时长:"+nanosAll+" ns,"+nanosAlls+"秒,商品池数量"+jsonArray.size()+",数量:"+allNum+"个skuId");
        return jsonArray;
    }
    /**
     * Disruptor 并发模式   提供者  消费者 
     */
    @Override
    public JSONArray syncCategoryNew3() {
    	
        JsonResult j = getPageNum();
        Stopwatch stopwatchAll =Stopwatch.createStarted();
        JSONArray jsonArray = j.getResult2();
        long allNum = 0L;
        try {
        	 String command = "new_category";
        	 HashMap<String,String> data = new HashMap();
             data.put("token",ACCESS_TOKEN);
             NewCategoryDataReq cateData = new NewCategoryDataReq(getSkuByPage,data,"jd.getSkuByPage");
             cateData.setParams(jsonArray);//过程数据
             cateData.setParamColumns(new String[] {"name","page_num"});//过程数据格式
			JdTransformTool.produce(cateData,dataSource, command,new JdDataPublisher());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        long nanosAll = stopwatchAll.elapsed(TimeUnit.NANOSECONDS);
        long nanosAlls = stopwatchAll.elapsed(TimeUnit.SECONDS);
        System.out.println("商品池请求编号全部获取时长:"+nanosAll+" ns,"+nanosAlls+"秒,商品池数量"+jsonArray.size()+",数量:"+allNum+"个skuId");
        return jsonArray;
    }
    /**
     * Disruptor 并发 消费者 
     */
    @Override
    public JSONArray syncCategoryNew2() {
    	
    	Stopwatch stopwatch = null;
    	JSONObject jsonObject = null;
        JsonResult j = getPageNum();
        Stopwatch stopwatchAll =Stopwatch.createStarted();
        JSONArray jsonArray = j.getResult2();
        long allNum = 0L;
        String name = "";
        String page_num = "";
        int skuNum =0;
        for (int i = 0,length =  jsonArray.size();i < length;i++){
       // int i= 0;
        	stopwatch =Stopwatch.createStarted();
             jsonObject = jsonArray.getJSONObject(i);
             name = jsonObject.getString("name");
             page_num = jsonObject.getString("page_num");
             skuNum = syncCategoryPage2(page_num);//skuId数量
            allNum += skuNum;
            long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
            System.out.println("商品池请求编号:【"+page_num+"】,第"+i+"个商品名称:" + name+" 数量:"+skuNum+"程序运行时间: "+nanos+"ns");
        }
        long nanosAll = stopwatchAll.elapsed(TimeUnit.NANOSECONDS);
        long nanosAlls = stopwatchAll.elapsed(TimeUnit.SECONDS);
        System.out.println("商品池请求编号全部获取时长:"+nanosAll+" ns,"+nanosAlls+"秒,商品池数量"+jsonArray.size()+",数量:"+allNum+"个skuId");
        return jsonArray;
    }
    
    public int syncCategoryPage2(String page_num) {
    	int skus = 0;
    	int page = 1;
    	JSONArray skuIds = null;
    	Stopwatch stopwatch = null;
    	//stopwatch =Stopwatch.createStarted();
    	JsonResult skuIdsResFirst  = getSkuByPage(page_num,page);
    	//long nanosfirst = stopwatch.elapsed(TimeUnit.NANOSECONDS);
    	if (skuIdsResFirst.get("result")==null) {
    		System.out.println("=====商品池编号:"+page_num+"为空");
    		return 0;
    	}
    	JSONObject skuIdsObjFirst =(JSONObject)skuIdsResFirst.get("result");
    	int pageCount = (int) skuIdsObjFirst.get("pageCount");
    	//skuIds = (JSONArray) skuIdsObjFirst.get("skuIds");
    	//skus += skuIds.size();
    	System.out.println("########pageCount:"+pageCount);
		//System.out.println("=====第"+page+"页skuId数量"+skuIds.size()+"个,pageCount:"+pageCount+",程序运行时间: "+nanosfirst+"ns");
		//if (pageCount>0) {
    	JsonResult skuIdsRes  = null;
    	long nanos = 0L;
    	JSONObject skuIdsObj = null;
    	NewCategoryData data = null;
    	String command = "new_category";
			for (int index = 1; index<=pageCount; index++) {
				stopwatch =Stopwatch.createStarted();
				 skuIdsRes  = getSkuByPage(page_num,index);
				 nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
				 skuIdsObj =(JSONObject)skuIdsRes.get("result");
				 if (skuIdsObj==null) {
					 continue;
				 }
				skuIds = (JSONArray) skuIdsObj.get("skuIds");
				 if (skuIds==null) {
					 continue;
				 }
				
				data = new NewCategoryData();
				data.setSkuIds(skuIds);
				data.setCategoryId(page_num);
				JdTransformTool.published(data,dataSource, command);
				System.out.println("########skuIds:"+skuIds);
				System.out.println("=====第"+index+"页skuId数量"+skuIds.size()+"个,pageCount:"+pageCount+"程序运行时间: "+nanos+"ns");
				skus += skuIds.size();
    		}
		//}
    	return skus;
    }
    
    private volatile int skuNum = 0;
    private volatile long allNum = 0L;
    /**
     * 线程池  消息提供者
     */
    @Override
    public JSONArray syncCategoryNew1() {
	   ExecutorService executor= Executors.newFixedThreadPool(4);
    	JSONObject jsonObject = null;
        JsonResult j = getPageNum();
        Stopwatch stopwatchAll =Stopwatch.createStarted();
        JSONArray jsonArray = j.getResult2();
       // String page_num = "";
        //int skuNum =0;
        //线程池
       final int size = jsonArray.size();
        CountDownLatch countDown = new CountDownLatch(4);
        for (int i = 0,length =  jsonArray.size();i < length;i++){
            // int i= 0;
             	final int s = i;
                  jsonObject = jsonArray.getJSONObject(i);
               // final String name = jsonObject.getString("name");
                  if(jsonObject==null) {
                 	 continue;
                  }
                 final String page_num = jsonObject.getString("page_num");
                 ProductManager.getInstance(size).setNum(page_num);
             }
             executor.execute(new Runnable() {
     			@Override
     			public void run() {
     				try {
     					while (true) {
     					Stopwatch stopwatch =Stopwatch.createStarted();
         				String page_num =ProductManager.getInstance(size).getNum();
         					if (page_num!=null) {
         						skuNum = syncCategoryPage(page_num);//skuId数量
         						allNum += skuNum;
         						long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
         						System.out.println("商品池请求编号:【"+page_num+"】 数量:"+skuNum+"程序运行时间: "+nanos+"ns");
							} else {
								countDown.countDown();
							}
     					}
     				} catch (InterruptedException e) {
						e.printStackTrace();
					}
     			}
             	
             });
             try {
				countDown.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        long nanosAll = stopwatchAll.elapsed(TimeUnit.NANOSECONDS);
        long nanosAlls = stopwatchAll.elapsed(TimeUnit.SECONDS);
        System.out.println("商品池请求编号全部获取时长:"+nanosAll+" ns,"+nanosAlls+"秒,商品池数量"+jsonArray.size()+",数量:"+allNum+"个skuId");
        return jsonArray;
    }
    /**
     * 直接获取 数据  单线程模式
     * @param page_num
     * @return
     */
    public int syncCategoryPage(String page_num) {
    	int skus = 0;
    	int page = 1;
    	JSONArray skuIds = null;
    	Stopwatch stopwatch = null;
    	//stopwatch =Stopwatch.createStarted();
    	JsonResult skuIdsResFirst  = getSkuByPage(page_num,page);
    	//long nanosfirst = stopwatch.elapsed(TimeUnit.NANOSECONDS);
    	if (skuIdsResFirst.get("result")==null) {
    		System.out.println("=====商品池编号:"+page_num+"为空");
    		return 0;
    	}
    	JSONObject skuIdsObjFirst =(JSONObject)skuIdsResFirst.get("result");
    	int pageCount = (int) skuIdsObjFirst.get("pageCount");
    	//skuIds = (JSONArray) skuIdsObjFirst.get("skuIds");
    	//skus += skuIds.size();
    	System.out.println("########pageCount:"+pageCount);
		//System.out.println("=====第"+page+"页skuId数量"+skuIds.size()+"个,pageCount:"+pageCount+",程序运行时间: "+nanosfirst+"ns");
		//if (pageCount>0) {
    	JsonResult skuIdsRes  = null;
    	long nanos = 0L;
    	JSONObject skuIdsObj = null;
			for (int index = 1; index<=pageCount; index++) {
				stopwatch =Stopwatch.createStarted();
				 skuIdsRes  = getSkuByPage(page_num,index);
				 nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
				 skuIdsObj =(JSONObject)skuIdsRes.get("result");
				 if (skuIdsObj==null) {
					 continue;
				 }
				skuIds = (JSONArray) skuIdsObj.get("skuIds");
				
				System.out.println("########skuIds:"+skuIds);
				System.out.println("=====第"+index+"页skuId数量"+skuIds.size()+"个,pageCount:"+pageCount+"程序运行时间: "+nanos+"ns");
				skus += skuIds.size();
    		}
		//}
    	return skus;
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
        List<JDBaseArea> areaList = new ArrayList<JDBaseArea>();
        //查询一级地址省
        Map<String, Object> provinceMap = this.getAllFirstJdAreaList();
        if(null != provinceMap) {
            for (String provinceName : provinceMap.keySet()) {
                if (StringUtils.isNotBlank(provinceName)
                        && null != provinceMap.get(provinceName) && StringUtils.isNumeric(provinceMap.get(provinceName).toString()) ) {

                    long provinceId = Long.parseLong(provinceMap.get(provinceName).toString());

                    JDBaseArea province = new JDBaseArea();

                    province.setAreaId(provinceId);
                    province.setAreaType("1");
                    province.setParentId(0L);
                    province.setAreaName(provinceName);
                    province.setAreaAllname(provinceName);
                    province.setDelete(true);

                    areaList.add(province);
                    //jdBaseAreaRepository.save(province);    //保存省


                    //查询二级地址 市
                    Map<String, Object> cityMap = cityMap = this.getSecondJdAreaList(provinceId);
                    /*//如果是直辖市
                    if(!CharteredCitiesList.contains(provinceName) ) {
                        cityMap = this.getSecondJdAreaList(provinceId);
                    } else {
                        cityMap = new HashMap<String, Object>();
                        cityMap.put(provinceName, provinceId);
                    }*/

                    if(null != cityMap) {

                        for (String cityName : cityMap.keySet()) {
                            if (StringUtils.isNotBlank(cityName)
                                    && null != cityMap.get(cityName) && StringUtils.isNumeric(cityMap.get(cityName).toString())) {

                                long cityId = Long.parseLong(cityMap.get(cityName).toString());

                                JDBaseArea city = new JDBaseArea();

                                city.setAreaId(cityId);
                                city.setAreaType("0");
                                city.setParentId(provinceId);//设置父级省ID
                                city.setAreaName(cityName);
                                city.setAreaAllname(cityName);
                                city.setDelete(true);

                                //jdBaseAreaRepository.save(city);    //保存市
                                areaList.add(city);


                                //查询三级地址 区(县)
                                Map<String, Object> countyMap = this.getThirdJdAreaList(cityId);
                                if(null != countyMap) {

                                    for (String countyName : countyMap.keySet()) {
                                        if (StringUtils.isNotBlank(countyName)
                                                && null != countyMap.get(countyName) && StringUtils.isNumeric(countyMap.get(countyName).toString())) {

                                            long countyId = Long.parseLong(countyMap.get(countyName).toString());

                                            JDBaseArea county = new JDBaseArea();

                                            county.setAreaId(countyId);
                                            county.setAreaType("2");
                                            county.setParentId(cityId); //设置父级市ID
                                            county.setAreaName(countyName);
                                            county.setAreaAllname(countyName);
                                            county.setDelete(true);

                                            areaList.add(city);
                                            //jdBaseAreaRepository.save(county);    //保存区(县)


                                            //查询四级地址 乡(镇)
                                            Map<String, Object> townMap = this.getThirdJdAreaList(countyId);
                                            if(null != townMap) {

                                                for (String townName : townMap.keySet()) {
                                                    if (StringUtils.isNotBlank(townName)
                                                            && null != townMap.get(townName) && StringUtils.isNumeric(townMap.get(townName).toString())) {

                                                        long townId = Long.parseLong(townMap.get(townName).toString());

                                                        JDBaseArea town = new JDBaseArea();

                                                        town.setAreaId(townId);
                                                        town.setAreaType("3");
                                                        town.setParentId(countyId);  //设置父级区(县)ID
                                                        town.setAreaName(townName);
                                                        town.setAreaAllname(townName);
                                                        town.setDelete(true);

                                                        //jdBaseAreaRepository.save(town);    //保存乡(镇)
                                                        areaList.add(town);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<String> sqlStrList = new ArrayList<String>();
        if(areaList.size() > 0) {

            for (JDBaseArea jdBaseArea : areaList) {
                StringBuilder bf = new StringBuilder();
                bf.append("insert into jd_base_area(area_id, area_allname, area_name, parent_id, area_type, is_delete) values")
                        .append("(")
                        .append(jdBaseArea.getAreaId())
                        .append(" ,\"").append(jdBaseArea.getAreaAllname())
                        .append("\", \"").append(jdBaseArea.getAreaName())
                        .append(", ").append(jdBaseArea.getParentId())
                        .append(", ").append(jdBaseArea.getAreaType())
                        .append(" , ").append(jdBaseArea.getDelete())
                        .append(");");

                sqlStrList.add(bf.toString());
            }


            int size = areaList.size();
            int batchNum = 1;

            int batchCount = new BigDecimal(size).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).intValue();
            int iterNum = 0;

            List<JDBaseArea> batchAreaList = new ArrayList<JDBaseArea>();

            /*for (int i = 0; i < size; i++) {
                batchAreaList.add(areaList.get(i));

                iterNum +=1;
                if(batchCount != batchNum) {
                    if ((i+1) % 100 == 0) {
                        batchNum += 1;
                        StringBuilder bf = new StringBuilder();
                        for (JDBaseArea baseArea : batchAreaList) {
                                bf.append("(")
                                        .append(baseArea.getAreaId())
                                        .append(",").append(baseArea.getAreaAllname())
                                        .append(",").append(baseArea.getAreaName())
                                        .append(",").append(baseArea.getParentId())
                                        .append(",").append(baseArea.getAreaType())
                                        .append(",").append(baseArea.getDelete())
                                        .append("),");
                        }

                        String sqls = bf.toString();
                        sqls = sqls.substring(0, sqls.length()-1);

                        //dataSource.executeUpdate("insert into jd_base_area(area_id, area_allname, area_name, parent_id, area_type, is_delete) values "+sqls, null);

                        //jdBaseAreaRepository.save(batchAreaList);
                        logger.error("\r\n\n\n =========== 第【" + batchNum + "】 批次 共计 【" + batchAreaList.size() + "】条地区数据插入 =========== \r\n\n\n");
                        batchAreaList.clear();
                    }
                } else {
                    if(size == (i+1)) {
                        jdBaseAreaRepository.save(batchAreaList);
                    }
                }
            }*/
        }

        logger.error("\r\n\n\n =========== 共计 【" + areaList.size() +"】条地区数据 =========== \r\n\n\n");

        return initSuccessResult(sqlStrList);
    }


    @Override
    public Map<String, Object> getAllFirstJdAreaList() {
        return this.getJdAreaList(this.getProvince, 0);
    }


    @Override
    public Map<String, Object> getSecondJdAreaList(long firstAreaId) {
        return this.getJdAreaList(this.getCity, firstAreaId);
    }


    @Override
    public Map<String, Object> getThirdJdAreaList(long secondAreaId) {
        return this.getJdAreaList(this.getCounty, secondAreaId);
    }


    @Override
    public Map<String, Object> getFourthJdAreaList(long thirdAreaId) {
        return this.getJdAreaList(this.getTown, thirdAreaId);
    }


    /**
     * 基础查询
     * @methodName baseParam
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:39
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     */
    private Map<String, Object> getJdAreaList(String suffixUrl, long areaId) {
        Map<String, Object> areaMap = new HashMap<String, Object>();

        try {
            if(StringUtils.isNotBlank(suffixUrl) ) {
                String url = this.jdOpenApiHost + suffixUrl;

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("token", ACCESS_TOKEN);

                if (0 < areaId) {
                    params.put("id", areaId);
                }

                String result = OKHttpClientUtil.httpPost(url, params);
                System.out.println(result);

                if (StringUtils.isNotBlank(result)) {

                    Map<String, Object> resultMap = JsonUtils.fromJson(result, Map.class);
                    if (null != resultMap.get("result")
                            && StringUtils.isNotBlank(resultMap.get("result").toString())) {

                        areaMap = (Map<String, Object>) resultMap.get("result");

                        for (String field : areaMap.keySet()) {
                            areaMap.get(field);
                            System.out.println("地区ID: " + areaMap.get(field) + " 名称: " + field);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return areaMap;
    }

	@Override
	public JsonResult getProductInfo(String skuId) {
		 
	        return null;
	}

	@Override
	public JsonResult syncCategoryDetail2() {
		return null;
	}

	@Override
	public JsonResult syncCategoryDetail() {
		return null;
	}


}
