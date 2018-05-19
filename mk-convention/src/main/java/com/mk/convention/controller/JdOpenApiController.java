package com.mk.convention.controller;


import com.alibaba.fastjson.JSONArray;
import com.mk.convention.meta.JsonResult;
import com.mk.convention.model.OrderSyncOrderRequest;
import com.mk.convention.service.JDOpenApiService;
import com.stylefeng.guns.core.util.ResponseCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class JdOpenApiController {

    @Autowired
    JDOpenApiService jdOpenApiService;

    private JsonResult checkData(String Message){
        JsonResult jsonResult =new JsonResult();
        jsonResult.setCode(ResponseCode.SERVER_ERROR);
        jsonResult.setMessage(Message);
        jsonResult.setResult(null);
        return jsonResult;
    }

    @RequestMapping(value = "/getAccessToken",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getItemsById() {
        return jdOpenApiService.getAccessToken();
    }

    /** 
    * @Description: 获取商品池id
    * @Param:  
    * @return:  
    * @Author: lzm
    * @Date: 2018/5/15 
    */ 
    @RequestMapping(value = "/getProductPoll",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getProductPoll() {
        return jdOpenApiService.getProductPoll();
    }

    @RequestMapping(value = "/syncCategory",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult syncCategory() {
        return jdOpenApiService.syncCategory();
    }
    /**
     * 批量拉取skuId 并保存skuIds 列表
     * @return
     */
    @RequestMapping(value = "/syncCategoryNew",method = RequestMethod.GET)
    @ResponseBody
    public JSONArray syncCategoryNew() {
        return jdOpenApiService.syncCategoryNew2();
    }
    /**
     * 根据数据库skuIds 拉取商品详情 并保存
     * @return
     * @throws InterruptedException 
     */
    @RequestMapping(value = "/syncCategoryDetail",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult syncCategoryDetail() throws InterruptedException {
        return jdOpenApiService.syncCategoryDetail2();
    }

    @RequestMapping(value = "/getSkuByPage",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getSkuByPage(@RequestParam(value = "pageNum") String pageNum , @RequestParam(value = "pageNo")Integer pageNo){
        JsonResult jsonResult ;

        if (pageNum != null && !pageNum.isEmpty())//
        {//
            if (pageNum != null && !pageNum.isEmpty()){//
                jsonResult =  jdOpenApiService.getSkuByPage(pageNum,pageNo);//
            }else{//
                jsonResult = checkData("页码(pageNo)不能为空");//
                return  jsonResult;//
            }
        }else{//
            jsonResult = checkData("商品池编号(pageNum)不能为空");//
            return   jsonResult;//
        }
        return jsonResult;
    }

    @RequestMapping(value = "/getDetail",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getDetail(@RequestParam("skuId") String sku){
        JsonResult jsonResult;
        if(null!=sku&&!sku.isEmpty()){
        jsonResult = jdOpenApiService.getDetail(sku,null);
        }else{
            jsonResult = checkData("商品编号不能爲空");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/getSkuState",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getSkuState(@RequestParam(value = "skuIds") String skuIds ){
        JsonResult jsonResult;

        if( null != skuIds &&!skuIds.isEmpty()){
            if (skuIds.substring(skuIds.length()-1,skuIds.length()).equals(",")){
                skuIds = skuIds.substring(0,skuIds.length()-1);
            }
                jsonResult=jdOpenApiService.getSkuSate(skuIds);
        }else{
            jsonResult=checkData("商品编号不能为空！");
        }
        return jsonResult;
    }

    @RequestMapping(value = "/getCategory",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getCategory(@RequestParam("cid") Long cid){
        JsonResult jsonResult;
        if (cid!=null){
            jsonResult = jdOpenApiService.getCategory(cid);
        }else{
            jsonResult = checkData("分类id不能为空");
        }
        return jsonResult;
    }


    @RequestMapping(value = "/getSellPrice",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getSellPrice(@RequestParam(value = "skuIds") String skuIds ){
        JsonResult jsonResult;

        if( null != skuIds &&!skuIds.isEmpty()){
            if (skuIds.substring(skuIds.length()-1,skuIds.length()).equals(",")){
                skuIds = skuIds.substring(0,skuIds.length()-1);
            }
            jsonResult=jdOpenApiService.getSellPrice(skuIds);
        }else{
            jsonResult=checkData("商品编号不能为空！");
        }
        return jsonResult;
    }


    @RequestMapping(value = "/getJDBaseArea",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getJDBaseArea(){
        return jdOpenApiService.saveAllJdArea();
    }

    /**
    * @Description: 统一下单接口 submitOrder
    * @Param:
    * @return:
    * @Author: Miaoxy
    * @Date: 5/8/2018
    **/
   /* @RequestMapping(value = "/submitOrder",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult submitOrder(){
        return jdOpenApiService.submitOrder();
    }*/

   /**
   * @Description: 获取商品图片
   * @Author: Miaoxy
   * @Date: 5/8/2018
   **/
   @RequestMapping(value = "/getSkuImage",method = RequestMethod.GET)
   @ResponseBody
   public JsonResult getSkuImage() throws InterruptedException {
       return jdOpenApiService.getSkuImage();
   }
    
   /** 
   * @Description: 获取商品地址
   * @Param:  
   * @return:  
   * @Author: Miaoxy 
   * @Date: 5/8/2018 
   **/
   @RequestMapping(value = "/getSkuAddress",method = RequestMethod.GET)
   @ResponseBody
   public JsonResult getSkuAddress() throws InterruptedException {
       return jdOpenApiService.getSkuAddress();
   }

    @RequestMapping(value = "/cancelOrder/{orderId}",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult cancelOrder(@PathVariable(value = "orderId") String jdOrderId){
        return jdOpenApiService.cancelOrder(jdOrderId);
    }


    @RequestMapping(value = "/submitOrder",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult submitOrder(@RequestBody OrderSyncOrderRequest orderSyncOrderRequest){
        return jdOpenApiService.submitOrder(orderSyncOrderRequest);
    }
    @RequestMapping(value = "/exportImg",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult exportImg(){
        return jdOpenApiService.exportImg();
    }

    @RequestMapping(value = "/exportIntroduceImg",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult exportIntroduceImg(){
        return jdOpenApiService.exportIntroduceImg();
    }
}
