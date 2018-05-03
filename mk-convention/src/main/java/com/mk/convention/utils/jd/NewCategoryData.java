package com.mk.convention.utils.jd;


import com.alibaba.fastjson.JSONArray;
/**
 * BaseDataEvent 请求 和 传递参数封装
 * @author lcc
 *
 */
public class NewCategoryData implements DataEvent,java.io.Serializable {
	
	
	private static final long serialVersionUID = -2622607039005848616L;
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
