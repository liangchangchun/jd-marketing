package com.mk.convention.utils.jd;

import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;

public class NewCategoryDataReq  extends BaseDataEvent implements DataEvent,java.io.Serializable{
	private static final long serialVersionUID = -2578921540515394256L;
	public NewCategoryDataReq(String method, HashMap<String, String> data, String logTag) {
		super(method, data, logTag);
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
}
