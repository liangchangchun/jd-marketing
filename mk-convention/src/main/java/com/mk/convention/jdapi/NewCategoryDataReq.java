package com.mk.convention.jdapi;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.utils.jd.BaseDataEvent;
import com.mk.convention.utils.jd.DataEvent;
import com.mk.convention.utils.jd.JDHttpTool;
import com.mk.convention.utils.jd.JdDataEvent;
import com.mk.convention.utils.jd.JdDataEventTranslator;

public class NewCategoryDataReq  extends BaseDataEvent implements DataEvent,java.io.Serializable{
	private static final long serialVersionUID = -2578921540515394256L;
	public NewCategoryDataReq() {
		
	}
	private JSONArray skuIds;//最终获取数据
	private String categoryId;
	
	public JSONArray getSkuIds() {
		return skuIds;
	}
	public void setSkuIds(JSONArray skuIds) {
		this.skuIds = skuIds;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
	public String adapterSql(JdDataEvent jdDataEvent) {
		NewCategoryDataReq dataEvent = (NewCategoryDataReq)jdDataEvent.getEvent();
		JSONArray skuids = dataEvent.getSkuIds();
        if(skuids==null){
            return null;
        }
        String category_id = dataEvent.getCategoryId();
        StringBuffer bf = new StringBuffer();
        for(int jj = 0 ;jj < skuids.size();jj++){
            String skuid = skuids.get(jj).toString();
            bf.append("(").append(skuid).append(",").append(category_id).append("),");
        }
        String sqls = bf.toString();
        sqls = sqls.substring(0, sqls.length()-1);
        //String tableName = dataEvent.getTableName();
        String buildSql = "insert into new_category(sku_id,category_id) values "+sqls;
		return buildSql;
	}
	


	
	@Override
	public void ApiRequest(JdDataEvent jdDataEvent,Disruptor<JdDataEvent> disruptor,JdDataEventTranslator eventTranslator) {
		BaseDataEvent req=(BaseDataEvent)jdDataEvent.getEvent();
			int skus = 0;
			int page = 1;
			 HashMap<String,String> paramData = req.getData();
			 if (paramData==null) {return;}
			 //String page_num = paramData.get("pageNum");
			 //String name =  paramData.get("name");
			 HashMap<String,String> data = new HashMap<String,String>();
		        data.put("token",paramData.get("token"));
		        data.put("pageNum",paramData.get("pageNum"));
		        data.put("pageNo",String.valueOf(page));
		  Stopwatch stopwatch = null;
		  JsonResult resFirst  = JDHttpTool.getInstance().packageParams(req.getMethod(),data,req.logTag);
	        if (resFirst.get("result")==null) {
	    		System.out.println(req.resultLog());
	    		return;
	    	}
	        JSONObject skuIdsObjFirst =(JSONObject)resFirst.get("result");
	    	int pageCount = (int) skuIdsObjFirst.get("pageCount");
	    	System.out.println("########pageCount:"+pageCount);
	    	JsonResult skuIdsRes  = null;
	    	long nanos = 0L;
	    	JSONObject skuIdsObj = null;
	    	NewCategoryDataReq dataReq = null;
				for (int index = 1; index<=pageCount; index++) {
					stopwatch =Stopwatch.createStarted();
					data.put("pageNo",String.valueOf(index));
					 skuIdsRes  = JDHttpTool.getInstance().packageParams(req.getMethod(),data,req.logTag);
					 nanos = stopwatch.elapsed(TimeUnit.NANOSECONDS);
					 skuIdsObj =(JSONObject)skuIdsRes.get("result");
					 if (skuIdsObj==null) {
						 continue;
					 }
					skuIds = (JSONArray) skuIdsObj.get("skuIds");
					 if (skuIds==null) {
						 continue;
					 }
					 dataReq = new NewCategoryDataReq();
					 dataReq.setTableName(req.getTableName());
					 dataReq.setMethod(req.getMethod());
					 dataReq.setData(data);
					 dataReq.setLogTag(req.logTag);
					 dataReq.setSkuIds(skuIds);
					 dataReq.setCategoryId(req.getData().get("pageNum"));
					 jdDataEvent.setEvent(dataReq);//更新内容
					//publish发布给消费者
					 super.publishEvent(jdDataEvent, disruptor, eventTranslator);
					 //long seq = disruptor.getRingBuffer().next();
					 //eventTranslator.translateTo(jdDataEvent, seq);
					 //disruptor.publishEvent(eventTranslator);
					System.out.println("########skuIds:"+skuIds);
					System.out.println("=====第"+index+"页skuId数量"+skuIds.size()+"个,pageCount:"+pageCount+"程序运行时间: "+nanos+"ns");
					skus += skuIds.size();
	    		}
	}
	
	@Override
	public String resultLog() {
		return "=====商品池编号:该商品池编号请求skuIds为空";
	}
	
	
	
}
