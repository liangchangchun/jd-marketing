package com.mk.convention.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;

public class CategoryManager {

	private AtomicInteger lastNumber = new AtomicInteger(0);
	private List<JSONObject> queueNumbers = new ArrayList<JSONObject>();
	
	public synchronized Integer addCategory(JSONObject obj){
		queueNumbers.add(obj);
		return lastNumber.incrementAndGet();
	}
	
	public synchronized JSONObject fetchCategory(){
		if(queueNumbers.size()>0){
			lastNumber.decrementAndGet();
			return (JSONObject)queueNumbers.remove(0);
		}else{
			return null;
		}
	}
}
