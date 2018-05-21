package com.mk.convention.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.mk.convention.dao.ProductPriceInfoMapper;
import com.mk.convention.jdapi.*;
import com.mk.convention.config.DataSource;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.model.Category;
import com.mk.convention.model.ProductPriceInfo;
import com.mk.convention.model.SkuByPage;
import com.mk.convention.model.entity.CategoryDocument;
import com.mk.convention.model.entity.ProductDetailDocument;
import com.mk.convention.model.*;
import com.mk.convention.respository.OrderRepository;
import com.mk.convention.respository.ProductPriceInfoRepository;
import com.mk.convention.respository.es.CategoryRepository;
import com.mk.convention.respository.es.ProductAreaRepository;
import com.mk.convention.respository.es.ProductDetailRepository;
import com.mk.convention.persistence.model.JDBaseArea;
import com.mk.convention.respository.JDBaseAreaRepository;
import com.mk.convention.respository.es.ProductImageRepository;
import com.mk.convention.service.HttpService;
import com.mk.convention.service.JDOpenApiService;
import com.mk.convention.utils.JDOpenApiUtils;
import com.mk.convention.utils.JsonUtils;
import com.mk.convention.utils.OKHttpClientUtil;
import com.mk.convention.utils.jd.JDHttpTool;
import com.mk.convention.utils.jd.JdDataPublisher;
import com.mk.convention.utils.jd.JdTransformTool;
import com.mk.convention.utils.jd.JdTransformTool.JdDataEventType;
import com.mk.convention.utils.traditional.ProductManager;
import com.stylefeng.guns.core.util.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
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
    private String ACCESS_TOKEN;

    //獲取商品所有商品類目信息
    @Value("${jd.openApi.getPageNum}")
    private String getPageNum;

    //获取池内商品编号
    @Value("${jd.openApi.getSkuByPage}")
    private String getSkuByPage ;

    //获取商品详情信息
    @Value("${jd.openApi.getDetail}")
    private  String getDetail;

    //获取商品图片
    @Value("${jd.openApi.getImage}")
    private String getImage;

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

    @Value("${jd.openApi.getRefreshToken}")
    private String getRefreshToken;

    @Value("${jd.refreshToken}")
    private String refreshToken;

    @Value("${jd.openApi.cancelOrder}")
    private String cancelOrder;

    @Value("${jd.openApi.submitOrder}")
    private String submitOrder;

    @Value("${jd.openApi.invoiceName}")
    private String invoiceName;

    @Value("${jd.openApi.invoicePhone}")
    private String invoicePhone;

    @Value("${jd.openApi.invoiceProvice}")
    private Integer invoiceProvice;

    @Value("${jd.openApi.invoiceCity}")
    private Integer invoiceCity;

    @Value("${jd.openApi.invoiceCounty}")
    private Integer invoiceCounty;

    @Value("${jd.openApi.invoiceAddress}")
    private String invoiceAddress;

    @Value("${jd.openApi.companyName}")
    private String companyName;

    @Value("${jd.openApi.invoiceState}")
    private Integer invoiceState;

    @Value("${jd.openApi.invoiceType}")
    private Integer invoiceType;

    @Value("${jd.openApi.selectedInvoiceTitle}")
    private Integer selectedInvoiceTitle;

    @Value("${jd.openApi.invoiceContent}")
    private Integer invoiceContent;

    @Value("${jd.openApi.paymentType}")
    private Integer paymentType;

    @Value("${jd.openApi.isUseBalance}")
    private Integer isUseBalance;

    @Value("${jd.openApi.submitState}")
    private Integer submitState;

    @Value("${jd.openApi.doOrderPriceMode}")
    private Integer doOrderPriceMode;

    @Value("${jd.openApi.bNeedGift}")
    private String bNeedGift;

    @Autowired
    private JDBaseAreaRepository jdBaseAreaRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductAreaRepository productAreaRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    //获取商品分类信息
    @Value("${jd.openApi.getCategory}")
    private String getCategory;

    @Autowired
    private HttpService commonHttpService;

    @PostConstruct public void init(){this.getRefreShToken();}

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
    public JsonResult getProductPoll(){
      HashMap<String, String> data = new HashMap<>();
        data.put("token",ACCESS_TOKEN);
        return packageParams(getPageNum,data,"jd.getPageNum");
        /*
       JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(200);
        jsonResult.setMessage(null);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","乐富购 数码类");
        jsonObject.put("page_num","3941696");
        jsonArray.add(jsonObject);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name","普通商品池2");
        jsonObject2.put("page_num","3907396");
        jsonArray.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name","家具日常");
        jsonObject3.put("page_num","3876133");
        jsonArray.add(jsonObject3);
        jsonResult.setResult(jsonArray);
        return jsonResult;*/
        
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
    /**
     * 拉取商品详情
     * @throws InterruptedException 
     */
	@Override
	public JsonResult syncCategoryDetail2() throws InterruptedException {
		elasticsearchTemplate.deleteIndex(ProductDetailDocument.class);
		 ArrayList results = this.dataSource.executeQuery("select count(*) cnt from new_category", null);
		 Object[] obj = (Object[]) results.get(0);
		 Integer count = obj[0]==null? 0 : Integer.parseInt(String.valueOf(obj[0]));
		 final int index = 50;
		 int page = 0;
		 int pageCount = count/index;
		 if (count%index>0) {
			 pageCount++; 
		 }
		 //ExecutorService executorService = Executors.newFixedThreadPool(10);
		 HashMap<String,String> paramData = new HashMap<String,String>();//请求接口参数
		 	paramData.put("token",ACCESS_TOKEN);
    	 	paramData.put("index",String.valueOf(index));
		 for (int i=0;i<pageCount;i++) {//数据查询一页作为一次任务  
		//	final int ci = i;
		//	 executorService.execute(new Runnable() {
		//		 public void run() {
			    	 	paramData.put("pageNum",String.valueOf(i*index));
			    	 	ProductDetailData cateData = new ProductDetailData();//请求接口
			         	cateData.setMethod(getDetail);//获取商品详情接口
			         	cateData.setData(paramData);
			         	cateData.setLogTag("jd.getSkuByPage");
						JdTransformTool.produce(cateData,productDetailRepository);
			}
			//});
		 //}
		JsonResult result = new JsonResult();
		result.setCode(200);
		result.setMessage("拉取数据保存完成");
		// executorService.shutdown();
		return result;
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

        JsonResult pageNum = this.getProductPoll();

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
                        if(skuByPage.getCode().equals("500")){
                            jsonResult = RefreShAccessToken(getSellPrice,data);
                        }
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
            productPriceInfoRepository.saveAll(productPriceInfos);
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
        try {
            long t1 = System.currentTimeMillis();

            // 调用京东open api
            String response = commonHttpService.doPost(url, params, "utf-8");
//            long t2 = System.currentTimeMillis();
//
//              JsonResult jsonResult = handleResponse(response);
//            if(jsonResult.getCode().equals("500")){
//                return RefreShAccessToken(method,data);
//            }
            return handleResponse(response);
        } catch (Exception e) {
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
        JsonResult j = getProductPoll();
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
                dataSource.executeUpdate("insert into category_img(sku_id,category_id) values "+sqls, null);
                if(skuids.size()>=500){
                    index++;
                }else{
                    isok = false;
                }
            }
        }
        return null;
    }
    
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    
	@Override
	public JSONArray syncCategoryNew2() {
		elasticsearchTemplate.deleteIndex(CategoryDocument.class);
		JSONObject jsonObject = null;
        JsonResult j = getProductPoll();
        Stopwatch stopwatchAll =Stopwatch.createStarted();
        JSONArray jsonArray = j.getResult2();
        //long allNum = 0L;
        String name = "";
        String page_num = "";
       // int skuNum =0;
        try {
        	int length = jsonArray.size();
        for (int i = 0;i < length;i++){
       // int i= 0;
        	//stopwatch =Stopwatch.createStarted();
             jsonObject = jsonArray.getJSONObject(i);
             name = jsonObject.getString("name");
             page_num = jsonObject.getString("page_num");
             //skuNum = syncCategoryPage2(page_num);//skuId数量
        	// String command = "JDBC_SAVE";
        	 HashMap<String,String> paramData = new HashMap<String,String>();//请求接口参数
        	 paramData.put("token",ACCESS_TOKEN);
        	 paramData.put("pageNum", page_num);
        	 paramData.put("name", name);
             CategoryData cateData = new CategoryData();//请求接口
             cateData.setMethod(getSkuByPage);//
             cateData.setData(paramData);
             cateData.setLogTag("jd.getSkuByPage");
             cateData.setTableName("new_category");
           //默认数据发布保存到数据库
			JdTransformTool.produce(cateData,categoryRepository);
            //allNum += skuNum;
           // long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
            // System.out.println("商品池请求编号:【"+page_num+"】,第"+i+"个商品名称:" + name+" 数量:"+skuNum+"程序运行时间: "+nanos+"ns");
        	}
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}

        long nanosAll = stopwatchAll.elapsed(TimeUnit.NANOSECONDS);
        long nanosAlls = stopwatchAll.elapsed(TimeUnit.SECONDS);
        System.out.println("商品池请求编号全部获取时长:"+nanosAll+" ns,"+nanosAlls+"秒,商品池数量"+jsonArray.size());
        return jsonArray;
	}
    
    @Override
    public JSONArray syncCategoryNew() {
    	Stopwatch stopwatch = null;
    	JSONObject jsonObject = null;
        JsonResult j = getProductPoll();
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
    	JSONObject jsonObject = null;
        JsonResult j = getProductPoll();
        Stopwatch stopwatchAll =Stopwatch.createStarted();
        JSONArray jsonArray = j.getResult2();
        //long allNum = 0L;
        String name = "";
        String page_num = "";
       // int skuNum =0;
        try {
        	int length = jsonArray.size();
        for (int i = 0;i < length;i++){
       // int i= 0;
        	//stopwatch =Stopwatch.createStarted();
             jsonObject = jsonArray.getJSONObject(i);
             name = jsonObject.getString("name");
             page_num = jsonObject.getString("page_num");
             //skuNum = syncCategoryPage2(page_num);//skuId数量
        	// String command = "JDBC_SAVE";
        	 HashMap<String,String> paramData = new HashMap<String,String>();//请求接口参数
        	 paramData.put("token",ACCESS_TOKEN);
        	 paramData.put("pageNum", page_num);
        	 paramData.put("name", name);
             NewCategoryDataReq cateData = new NewCategoryDataReq();//请求接口
             cateData.setMethod(getSkuByPage);//
             cateData.setData(paramData);
             cateData.setLogTag("jd.getSkuByPage");
             cateData.setTableName("new_category");
           //默认数据发布保存到数据库
			JdTransformTool.produce(cateData);
            //allNum += skuNum;
           // long nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
            // System.out.println("商品池请求编号:【"+page_num+"】,第"+i+"个商品名称:" + name+" 数量:"+skuNum+"程序运行时间: "+nanos+"ns");
        	}
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}

        long nanosAll = stopwatchAll.elapsed(TimeUnit.NANOSECONDS);
        long nanosAlls = stopwatchAll.elapsed(TimeUnit.SECONDS);
        System.out.println("商品池请求编号全部获取时长:"+nanosAll+" ns,"+nanosAlls+"秒,商品池数量"+jsonArray.size());
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
	public JsonResult syncCategoryDetail() {
		return null;
	}

    /**
     * 刷新京东token值
     * @Author liukun
     * @param
     * @return
     */
    @Override
    public JsonResult getRefreshToken(){
        HashMap<String,String> data = new HashMap();
        data.put("refresh_token",refreshToken);
        data.put("client_id",clientId);
        data.put("client_secret",clientSecret);
        return packageParams(getRefreshToken,data,"jd.getRefreshToken");
    }
    @Override
    public JsonResult RefreShAccessToken(String method, HashMap<String, String> data) {
        JsonResult jsonResult = getRefreshToken();
        Map map = (Map) JsonUtils.fromJson(JsonUtils.toJson(jsonResult), Map.class);
        String RefreShAccessToken = new String();
        if ((Integer) map.get("code") == 200) {
            Map resultMap = (Map) JsonUtils.fromJson(JsonUtils.toJson(map.get("result")), Map.class);
            if (StringUtils.isNotBlank((String) resultMap.get("access_token"))) {
                RefreShAccessToken = (String) resultMap.get("access_token");
                data.remove("token");
                data.put("token", RefreShAccessToken);
            }
        }

        return packageParams(method, data, method);
    }

    private void getRefreShToken(){
        JsonResult jsonResult = getRefreshToken();
        Map map = (Map) JsonUtils.fromJson(JsonUtils.toJson(jsonResult), Map.class);
        if ((Integer) map.get("code") == 200) {
            Map resultMap = (Map) JsonUtils.fromJson(JsonUtils.toJson(map.get("result")), Map.class);
            if (StringUtils.isNotBlank((String) resultMap.get("access_token"))) {
                ACCESS_TOKEN = (String) resultMap.get("access_token");
            }
        }
    }
    /**
    * @Description: 获取图片
    * @Param:
    * @return:
    * @Author: Miaoxy
    * @Date: 5/8/2018
    **/
    @Override
    public JsonResult getSkuImage() throws InterruptedException{
        HashMap<String,String> paramData = new HashMap<String,String>();//请求接口参数
        ProductImageData cateData = null;
        ArrayList results = this.dataSource.executeQuery("select count(*) cnt from new_category", null);
        Object[] obj = (Object[]) results.get(0);
        Integer count = obj[0]==null? 0 : Integer.parseInt(String.valueOf(obj[0]));
        int index = 500;
        int page = 0;
        int pageCount = count/index;
        if (count%index>0) {
            pageCount++;
        }
        for (int i=0;i<pageCount;i++) {//数据查询一页作为一次任务
            paramData.put("token",ACCESS_TOKEN);
            paramData.put("index",String.valueOf(index));
            paramData.put("pageNum",String.valueOf(i*index));

            cateData = new ProductImageData();//请求接口
            cateData.setMethod(getImage);//获取商品详情接口
            cateData.setData(paramData);
            cateData.setLogTag("jd.getSkuImage");
            JdTransformTool.produce(cateData,productImageRepository);
        }
        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setMessage("拉取数据保存完成");
        return result;
    }
    /** 
    * @Description: 获取基础地区表信息
    * @Author: Miaoxy 
    * @Date: 5/8/2018 
    **/
    @Override
    public JsonResult getSkuAddress() throws InterruptedException {
        HashMap<String,String> paramData = new HashMap<String,String>();
        ProductAreaData cateData = new ProductAreaData();
        paramData.put("token",ACCESS_TOKEN);
        cateData.setData(paramData);
        cateData.setLogTag("jd.getSkuAddress");
        cateData.setMethod(getProvince+","+getCity+","+getCounty+","+getTown);
        JdTransformTool.produce(cateData,productAreaRepository);

        JsonResult result = new JsonResult();
        result.setCode(200);
        result.setMessage("拉取数据保存完成");
        return result;
    }


    /**
     * @Description: 取消订单接口
     * @Param:
     * @return:
     * @Author: lzm
     * @Date: 2018/5/8
     */
    @Override
    public JsonResult cancelOrder(String jdOrderId) {
        HashMap<String,String> data = new HashMap<>();
        data.put("jdOrderId",jdOrderId);
        data.put("token",ACCESS_TOKEN);
        return packageParams(cancelOrder,data,"jd.cancelOrder");
    }


    /**
     * @Description: 统一下单 京东
     * @Param:  orderSyncOrderRequest
     * @return:  JsonResult
     * @Author: lzm
     * @Date: 2018/5/8
     */
    @Override
    public JsonResult submitOrder(OrderSyncOrderRequest orderSyncOrderRequest) {
        OrderJdInfo orderJdInfo = transformationOrderToJd(orderSyncOrderRequest);
        HashMap<String,String> data = new HashMap<>();
        try {
            data = objectToMap(orderJdInfo);
            data.put("token",ACCESS_TOKEN);
            logger.error("请求json字符串:" + JSONObject.toJSONString(data));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return packageParamsToOrder(submitOrder,data,"jd.submitOrder");
    }


    /**
     * @Description: 统一下单数据转换成京东的字段
     * @Param:  orderSyncOrderRequest
     * @return:  OrderJdInfo
     * @Author: lzm
     * @Date: 2018/5/8
     */
    public OrderJdInfo transformationOrderToJd(OrderSyncOrderRequest orderSyncOrderRequest){
        OrderAddressWms orderAddress = orderSyncOrderRequest.getOrderAddress();
        OrderWms orderWms = orderSyncOrderRequest.getOrder();
        List<OrderDetailsWms> orderDetails = orderSyncOrderRequest.getOrderDetails();
        OrderJdInfo orderJdInfo = new OrderJdInfo();
        orderJdInfo.setThirdOrder(orderSyncOrderRequest.getSaleOrderCode());
        JSONArray jsonArray = new JSONArray();
        if(orderDetails!=null){//SKU信息
            for (OrderDetailsWms orderDetailsWms : orderDetails){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("skuId",orderDetailsWms.getProductSku());
                jsonObject.put("num",orderDetailsWms.getQty());
                jsonObject.put("bNeedGift",bNeedGift);//是否需要赠品  需要设置开关
                jsonObject.put("bNeedAnnex",true);//是否需要附件
                jsonArray.add(jsonObject);
            }
        }
        String sku = jsonArray.toJSONString();
        if(orderAddress!=null){
            orderJdInfo.setName(orderAddress.getName());
            orderJdInfo.setProvince(2);//省份 测试 默认给了上海
            orderJdInfo.setCity(2830);//城市
            orderJdInfo.setCounty(51800);//三级地址
            orderJdInfo.setTown(0);//四级地址 (如果该地区有四级地址，则必须传递四级地址，没有四级地址则传 0)
            orderJdInfo.setAddress(orderAddress.getLine1());
            orderJdInfo.setZip(orderAddress.getPostalCode());
            orderJdInfo.setMobile(orderAddress.getPhone());
            orderJdInfo.setEmail(orderWms.getBuyerMail());//邮箱
        }
        orderJdInfo.setSku(sku);
        orderJdInfo.setInvoiceState(invoiceState);//开票方式(1 为随货开票，0 为订单预借，2 为集中开票 )
        orderJdInfo.setInvoiceType(invoiceType);//1 普通发票 2 增值税发票 3 电子发票
        orderJdInfo.setSelectedInvoiceTitle(selectedInvoiceTitle);//必须(发票类型：4 个人，5 单位)
        orderJdInfo.setCompanyName("上海乐辅电子商务有限公司");//发票抬头
        orderJdInfo.setInvoiceContent(invoiceContent);//:1明细，3：电脑配件，19:耗材，22：办公用品备注:若增值发票则只能选 1 明细
        orderJdInfo.setPaymentType(paymentType);//支付方式 (1：货到付款，2：邮局付款，4：余额支付，5：公司转账（公对公转账），7：网银钱包，101：金采支付)
        orderJdInfo.setIsUseBalance(isUseBalance);//使用余额 paymentType=4 时，此值固定是 1 其他支付方式 0
        orderJdInfo.setSubmitState(submitState);//是否预占库存，0 是预占库存（需要调用确认订单接口），1 是不预占库存
        orderJdInfo.setDoOrderPriceMode(doOrderPriceMode);//下单价格模式 0: 客户端订单价格快照不做验证对比，还是以京东价格正常下单;1:必需验证客户端单价格快照，如果快照与京东价格不一致返回下单失败，需要更新商品价格后，重新下单;
        orderJdInfo.setInvoiceName("财务部");//增值票收票人姓名
        orderJdInfo.setInvoicePhone(invoicePhone);//增值票收票电话
        orderJdInfo.setInvoiceProvice(invoiceProvice);//上海
        orderJdInfo.setInvoiceCity(invoiceCity);//浦东新区
        orderJdInfo.setInvoiceCounty(invoiceCounty);//书院镇
        orderJdInfo.setInvoiceAddress("浦东新区书院镇石潭街109号238室");
        return orderJdInfo;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public HashMap<String, String> objectToMap(Object obj) throws IllegalAccessException {
        HashMap<String, String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if(value == null){
                map.put(fieldName, "");
            }else{
                map.put(fieldName, value.toString());
            }
        }
        return map;
    }

    /**
     * @Author liukun
     * @param
     * @return
     */
    private JsonResult packageParamsToOrder(String method,HashMap<String, String> data, String logTag){

        TreeMap<String, String> paramsMap = null;
        paramsMap = JDOpenApiUtils.packageParams(data);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String url = jdOpenApiHost + method;
        try {
            long t1 = System.currentTimeMillis();

            // 调用京东open api
            String response = commonHttpService.doPost(url, params, "utf-8");
//            long t2 = System.currentTimeMillis();

//            JsonResult jsonResult = handleResponse(response);
//            if(jsonResult.getCode().equals("500")){
//                return RefreShAccessToken(method,data);
//            }
            return handleResponseToOrder(response);
        } catch (Exception e) {
            return initServerError("server error");
        }
    }

    private JsonResult handleResponseToOrder(String response) {

        if (StringUtils.isEmpty(response)) {
            if (null != logger) {
                logger.error(".handleResponse() response is empty");
            }
            return initServerError(".handleResponse() response is empty");
        }

        JSONObject responseObj = JSON.parseObject(response);

        String success = responseObj.getString("success");
        String message = responseObj.getString("resultMessage");
        if ("true".equals(success)) {
            return initSuccessResult(responseObj.get("result"));
        } else {
            if (null != logger) {
                logger.error(".handleResponse() fail. response={}", response);
            }
            return initServerError(message);
        }
    }


    @Override
    public JsonResult exportImg() {
        ArrayList results = this.dataSource.executeQuery("select * from category_upc", null);
//        for (Object obj:results){
//            System.out.println(JsonUtils.toJson(obj));
//        }
        for (int i = 0 ; i< results.size();i++){
            Object[] obj = (Object[]) results.get(i);
            String sku = (String) obj[1];
            HashMap<String,String> data = new HashMap<>();
            data.put("sku","4098931");
            data.put("token",ACCESS_TOKEN);
            JsonResult result = packageParams(getDetail,data,"jd.getDetail");

            if(result.getResult3()!=null){
                System.out.println(result.getResult3().toJSONString());
                String upc = result.getResult3().getString("upc");
                String sql ="update category_upc set upc = '"+upc+"' where sku_id = '"+sku+"'";
                this.dataSource.executeUpdate(sql,null);
            }else{
                System.out.println("查找不到图片:"+sku);
            }

        }
        return null;
    }

//    @Override
//    public JsonResult exportImg() {
//        ArrayList results = this.dataSource.executeQuery("select * from category_img", null);
////        for (Object obj:results){
////            System.out.println(JsonUtils.toJson(obj));
////        }
//        String path = "G:\\images";
//        for (int i = 0 ; i< results.size();i++){
//            Object[] obj = (Object[]) results.get(i);
//            String sku = (String) obj[1];
//            HashMap<String,String> data = new HashMap<>();
//            data.put("sku",sku);
//            data.put("token",ACCESS_TOKEN);
//            JsonResult result = packageParams(getImage,data,"jd.getImage");
//
//            if(result.getResult3()!=null){
//                System.out.println(result.getResult3().toJSONString());
//                JSONArray jsonArray = result.getResult3().getJSONArray(sku);
//                for (int j = 0 ; j < jsonArray.size();j++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(j);
//                    Integer isPrimary = jsonObject.getInteger("isPrimary");
//                    String url = "http://img30.360buyimg.com/popWaterMark/"+jsonObject.getString("path");
//                    if(isPrimary==1){
//                        downloadPicture(url,path+"\\"+sku+"\\"+sku+"_main.jpg",path+"\\"+sku);
//                    }else{
//                        downloadPicture(url,path+"\\"+sku+"\\"+sku+"_"+j+".jpg",path+"\\"+sku);
//                    }
//                }
//            }else{
//                System.out.println("查找不到图片:"+sku);
//            }
//
//        }
//        return null;
//    }

    @Override
    public JsonResult exportIntroduceImg() {
        ArrayList results = this.dataSource.executeQuery("select * from category_img where sku_id = '419703'", null);
//        for (Object obj:results){
//            System.out.println(JsonUtils.toJson(obj));
//        }
        String path = "G:\\introduce44";
        for (int i = 0 ; i< results.size();i++){
            Object[] obj = (Object[]) results.get(i);
            String sku = (String) obj[1];
            System.out.println(i+"DDDDDDDDDDDDD"+sku);
            HashMap<String,String> data = new HashMap<>();
            data.put("sku",sku);
            data.put("token",ACCESS_TOKEN);
            JsonResult result = packageParams(getDetail,data,"jd.getDetail");

            if(result.getResult3()!=null){
//                System.out.println(result.getResult3().toJSONString());
//                String productArea = result.getResult3().getString("productArea");
//                String saleUnit = result.getResult3().getString("saleUnit");
//                String weight = result.getResult3().getString("weight");
//                System.out.println("productArea:"+productArea+"---saleUnit:"+saleUnit+"---weight:"+weight);
//                String sql = "update category_img set productArea = '"+productArea+"',saleUnit='"+saleUnit+"',weight='"+weight+"' where sku_id = '"+sku+"'";
//                System.out.println(sql);
                //this.dataSource.executeUpdate(sql,null);


                String introduction = result.getResult3().getString("introduction");
                List<String> list = introductionToImage(introduction);
//                if(list.size()==0){
//                    System.out.println(introduction);
//                    System.out.println(sku+"-------------");
//                }
                for (int j = 0 ; j< list.size(); j++ ){
                    String urmImage = list.get(j);
                    String url = urmImage;
                    downloadPicture(url,path+"\\"+sku+"\\"+sku+"_introduction_"+j+".jpg",path+"\\"+sku);
                }
            }else{
                System.out.println("查找不到图片:"+sku);
            }

        }
        return null;
    }
    public static List<String> introductionToImage(String introduction){
        List<String> list = new ArrayList<>();
        while(introduction.indexOf("<img    src=")>-1){
            System.out.println("----------"+introduction);
            String urlImage = "";
            introduction = introduction.substring(introduction.indexOf("<img    src="),introduction.length());
            boolean isok = false;
            if((!isok)&&introduction.indexOf(".jpg")>0 ){
                if((introduction.indexOf(".jpg")+4)-(introduction.indexOf("<img    src=")+13)>110){

                }else{
                    isok = true;
                    urlImage = introduction.substring(introduction.indexOf("<img    src=")+13,introduction.indexOf(".jpg")+4);
                    introduction = introduction.substring(introduction.indexOf(".jpg")+4,introduction.length());
                }
            }
            if((!isok)&&introduction.indexOf(".gif")>0){
                if((introduction.indexOf(".gif")+4)-(introduction.indexOf("<img    src=")+13)>110){

                }else{
                    isok = true;
                    urlImage = introduction.substring(introduction.indexOf("<img    src=")+13,introduction.indexOf(".gif")+4);
                    introduction = introduction.substring(introduction.indexOf(".gif")+4,introduction.length());
                }
            }
            if((!isok)&&introduction.indexOf(".png")>0){
                if((introduction.indexOf(".png")+4)-(introduction.indexOf("<img    src=")+13)>110){

                }else{
                    isok = true;
                    urlImage = introduction.substring(introduction.indexOf("<img    src=")+13,introduction.indexOf(".png")+4);
                    introduction = introduction.substring(introduction.indexOf(".png")+4,introduction.length());
                }
            }
            if(urlImage!=null && urlImage.trim() !="" && urlImage.indexOf("http")<0){
                if(urlImage.indexOf("//")<0){
                    urlImage = "http://"+urlImage;
                }else{
                    urlImage = "http:"+urlImage;
                }
            }

//            if(introduction.indexOf(".jpg")+4 > introduction.indexOf("<img src=")+10){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg")+4,introduction.length());
//            }else if(introduction.indexOf(".jpg\" width")>0){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" width")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" width")+5,introduction.length());
//            }else if(introduction.indexOf(".jpg\" alt")>0 && introduction.indexOf(".jpg\" alt")>introduction.indexOf("<img src=")){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" alt")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" alt")+5,introduction.length());
//            }else if(introduction.indexOf(".jpg\" />")>0 && introduction.indexOf(".jpg\" />")>introduction.indexOf("<img src=")){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" />")+5,introduction.length());
//            }else if(introduction.indexOf(".jpg\" />")>0 && introduction.indexOf(".jpg\" />")>introduction.indexOf("<img src=")){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" />")+5,introduction.length());
//            }else if(introduction.indexOf("<img src=")>0 && introduction.indexOf(".jpg\" />")>0 ){
//                introduction = introduction.substring(introduction.indexOf("<img src="),introduction.length());
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" />")+5,introduction.length());
//            }else if(introduction.indexOf("<img src=")>0 && introduction.indexOf(".gif\" />")>0 ){
//                introduction = introduction.substring(introduction.indexOf("<img src="),introduction.length());
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".gif\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".gif\" />")+5,introduction.length());
//            }

//            System.out.println(urlImage);
            list.add(urlImage);

        }


        while(introduction.indexOf("<img src=")>-1){
//            System.out.println("----------"+introduction);
            String urlImage = "";
            introduction = introduction.substring(introduction.indexOf("<img src="),introduction.length());
            boolean isok = false;
            if((!isok)&&introduction.indexOf(".jpg")>0 ){
                if((introduction.indexOf(".jpg")+4)-(introduction.indexOf("<img src=")+10)>110){

                }else{
                    isok = true;
                    urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg")+4);
                    introduction = introduction.substring(introduction.indexOf(".jpg")+4,introduction.length());
                }
            }
            if((!isok)&&introduction.indexOf(".gif")>0){
                if((introduction.indexOf(".gif")+4)-(introduction.indexOf("<img src=")+10)>110){

                }else{
                    isok = true;
                    urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".gif")+4);
                    introduction = introduction.substring(introduction.indexOf(".gif")+4,introduction.length());
                }
            }
            if((!isok)&&introduction.indexOf(".png")>0){
                if((introduction.indexOf(".png")+4)-(introduction.indexOf("<img src=")+10)>110){

                }else{
                    isok = true;
                    urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".png")+4);
                    introduction = introduction.substring(introduction.indexOf(".png")+4,introduction.length());
                }
            }
            if(urlImage!=null && urlImage.trim() !="" && urlImage.indexOf("http")<0){
                if(urlImage.indexOf("//")<0){
                    urlImage = "http://"+urlImage;
                }else{
                    urlImage = "http:"+urlImage;
                }
            }

//            if(introduction.indexOf(".jpg")+4 > introduction.indexOf("<img src=")+10){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg")+4,introduction.length());
//            }else if(introduction.indexOf(".jpg\" width")>0){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" width")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" width")+5,introduction.length());
//            }else if(introduction.indexOf(".jpg\" alt")>0 && introduction.indexOf(".jpg\" alt")>introduction.indexOf("<img src=")){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" alt")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" alt")+5,introduction.length());
//            }else if(introduction.indexOf(".jpg\" />")>0 && introduction.indexOf(".jpg\" />")>introduction.indexOf("<img src=")){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" />")+5,introduction.length());
//            }else if(introduction.indexOf(".jpg\" />")>0 && introduction.indexOf(".jpg\" />")>introduction.indexOf("<img src=")){
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" />")+5,introduction.length());
//            }else if(introduction.indexOf("<img src=")>0 && introduction.indexOf(".jpg\" />")>0 ){
//                introduction = introduction.substring(introduction.indexOf("<img src="),introduction.length());
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".jpg\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".jpg\" />")+5,introduction.length());
//            }else if(introduction.indexOf("<img src=")>0 && introduction.indexOf(".gif\" />")>0 ){
//                introduction = introduction.substring(introduction.indexOf("<img src="),introduction.length());
//                urlImage = introduction.substring(introduction.indexOf("<img src=")+10,introduction.indexOf(".gif\" />")+4);
//                introduction = introduction.substring(introduction.indexOf(".gif\" />")+5,introduction.length());
//            }

//            System.out.println(urlImage);
            list.add(urlImage);

        }

//        for (String urlImage : list){
//            System.out.println(urlImage);
//        }
        return list;
    }

//    public static void main(String[] args) {
//        String introduction = "lass=\"ssd-widget-pic W14925793239497\" >\n" +
//                "    <img    src=\"https://img30.360buyimg.com/sku/jfs/t9355/346/982196853/235759/653efdb1/59b2666bNfed18fb3.gif\"    alt=\"NB P5 (32-60英寸) 电视挂架 电视架 电视机挂架";
//        System.out.println(introduction.indexOf("<img src="));
//        String urlImage = introduction.substring(introduction.indexOf("<img    src=")+13,introduction.length());
//        System.out.println(urlImage);
//
////        List<String> list = introductionToImage(introduction);
////        System.out.println(list.size());
//    }

    public static void downloadPicture(String urlImage,String imageName,String fielPath){
        try {
            System.out.println(urlImage+"++++++++++++++++++++++++++++++");
            URL url = new URL(urlImage);
            File filePackage = new File(fielPath);
            if(!filePackage.exists()){
                filePackage.mkdir();
            }
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            dataInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        String imageUrl = "https://img30.360buyimg.com/sku/jfs/t5836/210/3508479450/50413/1131bcd9/593e3160N355b62cd.png";
//        System.out.println(imageUrl.length());
////        downloadPicture("//img30.360buyimg.com/sku/jfs/t3277/143/5324791507/82706/62138e1a/586e0a19N6cde1bfc.jpg","G:\\images\\131665\\dsfldj.jpg","G:\\images\\131665");
//    }

}
