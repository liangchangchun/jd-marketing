package com.mk.convention.jdapi;


import com.alibaba.fastjson.JSONArray;
import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.utils.jd.BaseDataEvent;
import com.mk.convention.utils.jd.DataEvent;
import com.mk.convention.utils.jd.JdDataEvent;
import com.mk.convention.utils.jd.JdDataEventTranslator;
import com.mk.convention.utils.jd.JdbcSqlAdapter;
/**
 * BaseDataEvent 请求 和 传递参数封装
 * @author lcc
 *
 */
public class NewCategoryData extends BaseDataEvent implements DataEvent,java.io.Serializable {
	
	
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
	
	@Override
	public JdbcSqlAdapter adapterSql(JdDataEvent jdDataEvent) {
		NewCategoryData dataEvent = (NewCategoryData)jdDataEvent.getEvent();
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
        String buildSql = "insert into "+this.getTableName()+"(sku_id,category_id) values "+sqls;
        JdbcSqlAdapter sqla = new JdbcSqlAdapter();
        sqla.setSql(buildSql);
        sqla.setParameters(null);
		return sqla;
	}
	
	@Override
	public void ApiRequest(JdDataEvent jdDataEvent,Disruptor<JdDataEvent> disruptor,JdDataEventTranslator eventTranslator) {
		// TODO Auto-generated method stub
	}
	
}
