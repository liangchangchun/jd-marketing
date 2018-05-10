package com.mk.convention.jdapi;

import java.util.ArrayList;
import java.util.HashMap;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.model.entity.ProductDetailDocument;
import com.mk.convention.respository.es.ProductDetailRepository;
import com.mk.convention.utils.SpringUtil;
import com.mk.convention.utils.jd.BaseDataEvent;
import com.mk.convention.utils.jd.DataEvent;
import com.mk.convention.utils.jd.JDHttpTool;
import com.mk.convention.utils.jd.JdDataEvent;
import com.mk.convention.utils.jd.JdDataEventTranslator;
import com.mk.convention.utils.jd.JdbcSqlAdapter;

/**
 * 商品详情接口 数据拉取 和保存
 * @author lcc
 *
 */
public class ProductDetailData  extends BaseDataEvent implements DataEvent,java.io.Serializable{

	private static final long serialVersionUID = 3278877428633287718L;
	/**
	 * 索引存储格式
	 */
	private ProductDetailDocument product;
	
	/**
	 * 根据数据商品skuId 请求商品信息
	 * 一个任务 请求 一定量的 商品详情  index数量代表一个线程内请求n次http请求
	 */
	@Override
	public void ApiRequest(JdDataEvent jdDataEvent, Disruptor<JdDataEvent> disruptor,
			JdDataEventTranslator eventTranslator) {
		BaseDataEvent req=(BaseDataEvent)jdDataEvent.getEvent();
		HashMap<String,String> paramData = req.getData();
		 if (paramData==null) {return;}
		 //String page_num = paramData.get("pageNum");
		 //String name =  paramData.get("name");
		 HashMap<String,String> data = new HashMap<String,String>();
		/* ArrayList results = jdDataEvent.getDataSource().executeQuery("select count(*) cnt from new_category", null);
		 Object[] obj = (Object[]) results.get(0);
		 Integer count = obj[0]==null? 0 : Integer.parseInt(String.valueOf(obj[0]));
		 int index = 500;
		 int page = 0;
		 int pageCount = count/index;
		 if (count%index>0) {
			 pageCount++; 
		 }*/
		 int index = Integer.parseInt(paramData.get("index"));
		 int pageNum = Integer.parseInt(paramData.get("pageNum"));
		 ArrayList categorys = null;
		// String[] skuIds = null;//new String[] {"1103264","1078252352","5934106","2014612","18648162421"};
			 categorys = jdDataEvent.getDataSource().executeQuery("select id,sku_id from new_category  where id > "+pageNum+" order by id asc limit 0,"+index, null);
			 data.put("token",paramData.get("token"));
	     	//skuId 需要从数据库拉取获取
			 for (int j=0,jlen =categorys.size() ; j<jlen ; j++) {
				 Object[] category = (Object[]) categorys.get(j);
				 String sku =  String.valueOf(category[1]);
				 data.put("sku", sku);
	     		//Stopwatch stopwatch = null;
	     		JsonResult resFirst  = JDHttpTool.getInstance().packageParams(req.getMethod(),data,req.logTag);//请求商品详情 
	     		if (resFirst.get("result")==null) {
	     			System.out.println(req.resultLog());
	     			continue;
	     		}
	     		JSONObject objproduct =(JSONObject)resFirst.get("result");
	     		System.out.println("===sku:"+objproduct);
	     		product = new ProductDetailDocument();
	     		product.setSkuId(sku);
	     		product.setInfo(String.valueOf(resFirst.get("result")));
	     		jdDataEvent.setEvent(product);
	     		//** 设置索引dao层
	     		super.publishEvent(jdDataEvent, disruptor, eventTranslator);
			 }
	     
	}

	/**
	 * 保存商品详情
	 */
	/*
	@Override
	public JdbcSqlAdapter adapterSql(JdDataEvent jdDataEvent) {
		ProductDetailData dataEvent = (ProductDetailData)jdDataEvent.getEvent();
		JSONObject product = dataEvent.getProducts();
        if(product==null){
            return null;
        }
       System.out.println("===sku:"+product);
     
      //  sku_id,spu_id,warehouse_id,sku_type,author,editer,drawer,proofreader,isbn,publishers,sheet,pages,package,publish_Time,printeger_no,pack_num,language,papers,brand,comments,image,content_desc,related_products ,editer_desc,catalogue,book_abstract,author_desc,integerroduction
        String sqls = "";
        if (product.get("isbn")!=null) {//书籍
        	String[] columns= {"sku","spu_id","warehouse_id","sku_type","author","editer","drawer","proofreader","isbn","publishers","sheet","pages","package","publish_Time","printeger_no","pack_num","language","papers","brand","comments","image","content_desc","related_products ","editer_desc","catalogue","book_abstract","author_desc","integerroduction"};
        	System.out.println("===书sku:"+product);
        	//sqls = "insert into product_book_info(sku_id,spu_id,warehouse_id,sku_type,author,editer,drawer,proofreader,isbn,publishers,sheet,pages,package,publish_Time,printeger_no,pack_num,language,papers,brand,comments,image,content_desc,related_products,editer_desc,catalogue,book_abstract,author_desc,integerroduction)"+sqls;
        } else if (product.get("director")!=null) {//电影
        	
        }
        
        //String tableName = dataEvent.getTableName();
        JdbcSqlAdapter sqla = new JdbcSqlAdapter();
        sqla.setSql(sqls);
        sqla.setParameters(null);
		return null;
	}
	*/

}
