package com.mk.convention.jdapi;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.model.entity.ProductImageDocument;
import com.mk.convention.utils.jd.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: lovego-jd
 * @description: 商品图片拉取
 * @author: Miaoxy
 * @create: 2018-05-08 16:32
 **/
public class ProductImageData extends BaseDataEvent implements DataEvent,java.io.Serializable{

    private static final long serialVersionUID = -762867753764759647L;
    /**
     * 索引存储格式
     */
    private ProductImageDocument productImageDocument;

    @Override
    public void ApiRequest(JdDataEvent jdDataEvent, Disruptor<JdDataEvent> disruptor, JdDataEventTranslator eventTranslator) {
        BaseDataEvent req=(BaseDataEvent)jdDataEvent.getEvent();
        HashMap<String,String> paramData = req.getData();
        if (paramData==null) {return;}

        HashMap<String,String> data = new HashMap<String,String>();

        int index = Integer.parseInt(paramData.get("index"));
        int pageNum = Integer.parseInt(paramData.get("pageNum"));
        ArrayList categorys = null;
        // String[] skuIds = null;//new String[] {"1103264","1078252352","5934106","2014612","18648162421"};
        categorys = jdDataEvent.getDataSource().executeQuery("select id,sku_id from new_category  where id > "+pageNum+" order by id asc limit 0,"+index, null);
        data.put("token",paramData.get("token"));
        //skuId 需要从数据库拉取获取
        for (int j=0,jlen =categorys.size(); j<jlen ; j++) {
            Object[] category = (Object[]) categorys.get(j);
            String sku =  String.valueOf(category[1]);
            data.put("sku", sku);
            //Stopwatch stopwatch = null;
            JsonResult resFirst  = JDHttpTool.getInstance().packageParams(req.getMethod(),data,req.logTag);//请求商品图片
            if (resFirst.get("result")==null) {
                System.out.println(req.resultLog());
                continue;
            }
            JSONObject objproduct =(JSONObject)resFirst.get("result");
            System.out.println("===sku:"+objproduct);
            productImageDocument = new ProductImageDocument();
            productImageDocument.setSkuId(sku);
            productImageDocument.setImage(String.valueOf(resFirst.get("result")));
            jdDataEvent.setEvent(productImageDocument);
            //** 设置索引dao层
            super.publishEvent(jdDataEvent, disruptor, eventTranslator);
        }
    }
}
