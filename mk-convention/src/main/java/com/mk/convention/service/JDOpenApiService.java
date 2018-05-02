package com.mk.convention.service;

import com.alibaba.fastjson.JSONArray;
import com.mk.convention.meta.JsonResult;

import java.util.HashMap;
import java.util.Map;

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
   
   JsonResult getProductInfo(String skuId);

   /**
    * 获取商品上下架状态
    * @Author liukun
    * @param
    * @return
    */
   JsonResult getSkuSate(String skuIds);
   JsonResult syncCategoryDetail2();
   JsonResult syncCategoryDetail();
   JsonResult syncCategory();
   JSONArray syncCategoryNew();
   JSONArray syncCategoryNew1();
   JSONArray syncCategoryNew2();
   public JSONArray syncCategoryNew3();
   JsonResult getSellPrice(String skuIds);

    /**
     * 获取京东所有的地址并入库
     * @param
     * @return com.mk.convention.meta.JsonResult
     * @throws
     * @methodName saveAllJdArea
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:14
     */
    JsonResult saveAllJdArea();


    /**
     * 获取所有一级地区
     * @methodName saveAllFirstJdArea
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:33
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     */
    Map<String, Object> getAllFirstJdAreaList();


    /**
     * 查询所有一级地区下的二级地区
     * @param firstAreaId 一级地区ID
     * @methodName getSecondJdAreaList
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:33
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     */
    Map<String, Object> getSecondJdAreaList(long firstAreaId);


    /**
     * 查询所有二级地区下的三级地区
     * @param secondAreaId 二级地区ID
     * @methodName getThirdJdAreaList
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:33
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     */
    Map<String, Object> getThirdJdAreaList(long secondAreaId);

    /**
     * 查询所有三级地区下的四级地区
     * @param thirdAreaId 三级地区ID
     * @methodName getFourthJdAreaList
     * @author xiaosq@lovego.com
     * @date 2018/4/28 15:33
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @throws
     */
    Map<String, Object> getFourthJdAreaList(long thirdAreaId);

   /**
    * 获取分类信息
    * @Author liukun
    * @param id (可通过商品详情接口查询)
    * @return
    */
   JsonResult getCategory(Long id);
}
