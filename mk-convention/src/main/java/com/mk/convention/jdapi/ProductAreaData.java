package com.mk.convention.jdapi;

import com.lmax.disruptor.dsl.Disruptor;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.model.entity.BaseArea;
import com.mk.convention.model.entity.ProductAreaDocument;
import com.mk.convention.utils.jd.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: lovego-jd
 * @description: 地址信息拉取
 * @author: Miaoxy
 * @create: 2018-05-09 11:56
 **/
@Slf4j
public class ProductAreaData  extends BaseDataEvent implements DataEvent,java.io.Serializable{


    private static final long serialVersionUID = 274586513206307826L;

    private ProductAreaDocument product;

    @Override
    public void ApiRequest(JdDataEvent jdDataEvent, Disruptor<JdDataEvent> disruptor, JdDataEventTranslator eventTranslator) {
        BaseDataEvent req=(BaseDataEvent)jdDataEvent.getEvent();
        HashMap<String,String> paramData = req.getData();
        if (paramData==null) {return;}
        HashMap<String,String> data = new HashMap<String,String>();
        String[] split = req.getMethod().split(",");
        JsonResult resFirst  = JDHttpTool.getInstance().packageParams(split[0],paramData,req.getLogTag());//请求商品图片
        if (resFirst.get("result")==null) {
            System.out.println(req.resultLog());
            return;
        }
        Map<String, Object> map = resFirst.getResult3().getInnerMap();
        BaseArea baseArea=null;

        for(Object obj : map.keySet()) {
            String key = (String) obj;
            String value = map.get(key).toString();
            //获取第一层的id
            paramData.put("id",value);
            baseArea = new BaseArea();
            baseArea.setAreaName(key);
            baseArea.setId(value);
            baseArea.setParentId("0");
            product= new ProductAreaDocument();
            product.setAreaId(value);
            product.setBaseArea(baseArea.toString());
            jdDataEvent.setEvent(product);
            super.publishEvent(jdDataEvent, disruptor, eventTranslator);
            log.info("=============第一层地址resFirst=============="+product);
            JsonResult ressecond  = JDHttpTool.getInstance().packageParams(split[1],paramData,req.getLogTag());//请求商品图片
            for (Object obj3 : ressecond.getResult3().getInnerMap().keySet()) {
                String key3 = (String) obj3;
                paramData.put("id", ressecond.getResult3().getInnerMap().get(key3).toString());
                baseArea = new BaseArea();
                baseArea.setAreaName(key3);
                baseArea.setId(paramData.get("id"));
                baseArea.setParentId(value);
                product = new ProductAreaDocument();
                product.setAreaId(paramData.get("id"));
                product.setBaseArea(baseArea.toString());
                jdDataEvent.setEvent(product);
                super.publishEvent(jdDataEvent, disruptor, eventTranslator);
                log.info("==========第二层地址ressecond" + product);
                JsonResult resThird = JDHttpTool.getInstance().packageParams(split[2], paramData, req.getLogTag());
                if (resThird.getResult3() != null && resThird.getResult3().getInnerMap() != null) {
                for (Object obj4 : resThird.getResult3().getInnerMap().keySet()) {
                    String key4 = (String) obj4;
                    paramData.put("id", resThird.getResult3().getInnerMap().get(key4).toString());
                    baseArea = new BaseArea();
                    baseArea.setAreaName(key4);
                    baseArea.setId(paramData.get("id"));
                    baseArea.setParentId(ressecond.getResult3().getInnerMap().get(key3).toString());
                    product = new ProductAreaDocument();
                    product.setAreaId(paramData.get("id"));
                    product.setBaseArea(baseArea.toString());
                    jdDataEvent.setEvent(product);
                    super.publishEvent(jdDataEvent, disruptor, eventTranslator);
                    log.info("===========第三层地址resThird============" + product);
                    if (resThird.getResult3().getInnerMap().get(obj4.toString()) != null) {
                        JsonResult resFourth = JDHttpTool.getInstance().packageParams(split[3], paramData, req.getLogTag());
                        if (resFourth.getResult3() != null && resFourth.getResult3().getInnerMap() != null) {
                            for (Object obj5 : resFourth.getResult3().getInnerMap().keySet()) {
                                String key5 = (String) obj5;
                                paramData.put("id", resFourth.getResult3().getInnerMap().get(key5).toString());
                                baseArea = new BaseArea();
                                baseArea.setAreaName(key5);
                                baseArea.setId(paramData.get("id"));
                                baseArea.setParentId(resThird.getResult3().getInnerMap().get(key4).toString());
                                product = new ProductAreaDocument();
                                product.setAreaId(paramData.get("id"));
                                product.setBaseArea(baseArea.toString());
                                jdDataEvent.setEvent(product);
                                super.publishEvent(jdDataEvent, disruptor, eventTranslator);
                                log.info("=========第四层地址resFourth===========" + product);
                            }
                        }
                    }
                }
                }
            }
        }
    }
}
