package com.mk.convention.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class CategoryManager {

	private int lastNumber = 0;
	private List<JSONObject> queueNumbers = new ArrayList<JSONObject>();
	
	public synchronized Integer addCategory(JSONObject obj){
		queueNumbers.add(obj);
		return lastNumber;
	}
	
	public synchronized JSONObject fetchCategory(){
		if(queueNumbers.size()>0){
			return (JSONObject)queueNumbers.remove(0);
		}else{
			return null;
		}
	}
}
