package com.mk.convention.utils.jd;

import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.lmax.disruptor.dsl.Disruptor;

public abstract class BaseDataEvent {

	protected JSONArray params;
	protected String[] paramColumns;
	/**
	 * 表名
	 */
	protected String tableName;
	/**
	 * 请求方法
	 */
	protected String method;
	/**
	 * 请求数据
	 */
	protected HashMap<String, String> data; 
	
	public String logTag;
	
	public BaseDataEvent() {

	}
	/**
	 * 适配sql语句  消费者
	 * @return
	 */
	public abstract String adapterSql(JdDataEvent jdDataEvent);
	/**
	 * api请求  数据提供者
	 * @param jdDataEvent
	 * @param disruptor
	 * @param eventTranslator
	 */
	public abstract void ApiRequest(JdDataEvent jdDataEvent,Disruptor<JdDataEvent> disruptor,JdDataEventTranslator eventTranslator);
	/**
	 * 获取数据为空的时候打印
	 * @return
	 */
	public String resultLog(){
			return "该http请求result结果为空";
	};
	
	public void publishEvent(JdDataEvent jdDataEvent,Disruptor<JdDataEvent> disruptor,JdDataEventTranslator eventTranslator) {
		 long seq = disruptor.getRingBuffer().next();
		 eventTranslator.translateTo(jdDataEvent, seq);
		 disruptor.publishEvent(eventTranslator);
	}
	
	public JSONArray getParams() {
		return params;
	}
	public void setParams(JSONArray params) {
		this.params = params;
	}
	public String[] getParamColumns() {
		return paramColumns;
	}
	public void setParamColumns(String[] paramColumns) {
		this.paramColumns = paramColumns;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public HashMap<String, String> getData() {
		return data;
	}
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
	public String getLogTag() {
		return logTag;
	}
	public void setLogTag(String logTag) {
		this.logTag = logTag;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
