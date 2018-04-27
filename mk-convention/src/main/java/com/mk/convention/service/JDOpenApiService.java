package com.mk.convention.service;

import com.mk.convention.meta.JsonResult;

import java.util.HashMap;

public interface JDOpenApiService {

    /**
     * 获取 京东AccessToken
     * @Author liukun
     * @param
     * @return
     */
    JsonResult getAccessToken();

    /**
     * 获取京东商品池信息
     * @Author liukun
     * @param
     * @return
     */
    JsonResult getPageNum();

    /**
     * 获取京东商品池内商品编号接口-品类商品池
     * @Author liukun
     * @param PageNum(商品池编号),pageNo(页码，默认取第一页；每页最多 10000 条数据，品类商品池可能存在多页数据)
     * @return
     */
    JsonResult getSkuByPage(String PageNum ,Integer pageNo);

   /**
    *
    * @Author liukun
    * @param skuId 商品编号 只支持单个查询
    *              可选扩展参数，支持单个/多个查询[逗号间隔]：
    *              ,appintroduce：移动商品详情介绍信息
    *              shouhou：商品售后信息 ,
    *              isFactoryShip  是否厂商直送,
    *              isEnergySaving 是否政府节能,
    *              contractSkuExt 定制商品池开关,
    *              ChinaCatalog 中图法分类号
    * @return
    */
   JsonResult getDetail(String skuId, HashMap<String,String> data);

   JsonResult getSkuSate(String skuIds);

}
