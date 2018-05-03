package com.mk.convention.utils.jd;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.meta.JsonResult;

public class JdDataPublisher extends BasePublisher implements Runnable{

	public JdDataPublisher() {
	}

	@Override
	public void run() {
		//int skus = 0;
		JSONArray skuIds = null;
		Stopwatch stopwatch = null;
		JdDataEventTranslator eventTranslator = new JdDataEventTranslator();
		JdDataEvent jdDataEvent = this.getJdDataEvent();
		BaseDataEvent req=(BaseDataEvent)jdDataEvent.getEvent();
	   try {
		   req.ApiRequest(jdDataEvent, disruptor, eventTranslator);
		   /*
		   HashMap<String, String>  data = req.adapterApiParam(req.getData());
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
					 long seq = disruptor.getRingBuffer().next();
					 jdDataEvent.setEvent(dataReq);//更新内容
					 eventTranslator.translateTo(jdDataEvent, seq);
					 disruptor.publishEvent(eventTranslator);
					System.out.println("########skuIds:"+skuIds);
					System.out.println("=====第"+index+"页skuId数量"+skuIds.size()+"个,pageCount:"+pageCount+"程序运行时间: "+nanos+"ns");
					skus += skuIds.size();
	    		}*/
				/*
				for(int i = 0; i<NUM;i++){
					disruptor.publishEvent(eventTranslator);
					Thread.sleep(1000); // 假设一秒钟进一辆车
				}*/
			} finally{
				countDownLatch.countDown();//执行完毕后通知 await()方法
				//System.out.println("+==========+获取商品池编号:"+page_num+",名称:【"+name+"】,数量:【"+skus+"】数据都获取到!");
			}
		}

	}

