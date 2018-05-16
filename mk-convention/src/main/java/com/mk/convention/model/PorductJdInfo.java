package com.mk.convention.model;

import lombok.Data;

import java.util.List;

/** 
* @Description: 京东同步商品
* @Param:  
* @return:  
* @Author: lzm
* @Date: 2018/5/15 
*/ 
@Data
public class PorductJdInfo {

    private String spuCode;
    private Integer productType;//产品类型：0：普通商品，1：虚拟商品，2：服务
    private Integer isCombined;//是否组合商品
    private Integer isPresent;//是否为赠送商品 0 否 1 是
    private String brandName;//品牌
    /**
     * 是否包邮，0：否，1：是
     */
    private Integer freeposted;
    /**
     * spu名称
     */
    private String spuName;
    /**
     * 产品副标题
     */
    private String subtitle;
    /**
     * 售卖渠道，0：线上线下，1：仅线上，2仅线下
     */
    private Integer saleChannel;
    /**
     *贸易方式：0：完税，1：保税，2：直邮
     */
    private Integer tradeWay;
    /**
     * 经营方式：0：代销，1：购销
     */
    private Integer operatingWay;
    /**
     * 产地
     */
    private Long originId;

    private String originName;

    /**
     * 是否支持7天无理由退换,0：否，1：是
     */
    private Integer isHebdomad;
    /**
     * 质检方式，0：全检，1：免检，2：抽检
     */
    private Integer qualityTestWay;
    /**
     * 采购发票，0：无票，1：增值税发票，2：普通发票
     */
    private Integer purchaseInvoice;
    /**
     * 销售发票，0：无票，1：增值税发票，2：普通发票
     */
    private Integer saleInvoice;
    /**
     * 是否有保质期，0：无，1：有
     */
    private Integer isDurabilityPeriod;
    /**
     * 保质期时间
     */
    private Long durabilityPeriodTime;
    /**
     * 保质期单位 0：天，1：月，2：年
     */
    private Integer durabilityPeriodUnit;
    /**
     * 存储条件
     */
    private String storeCondition;
    /**
     * 一级产品分类
     */
    private Long oneCategoryId;

    private String oneCategoryName;
    /**
     * 二级产品分类
     */
    private Long twoCategoryId;

    private String twoCategoryName;
    /**
     * 三级产品分类
     */
    private Long threeCategoryId;


    private String threeCategoryName;
    /**
     * 发货方式，0：平台发货，1一件代发
     */
    private Integer sendWay;
    /**
     *站内关键字
     */
    private String keywords;
    /**
     * SEO关键词
     */
    private String seoKeywords;
    /**
     * SEO标题
     */
    private String seoTitle;
    /**
     * SEO描述
     */
    private String seoDescribe;
    /**
     * 是否设置可售区域，0：没有区域限制，1：有区域限制
     */
    private Integer isSaleArea;

    /**
     * 产品说明
     */
    private String illustrate;
    /**
     * 商品渠道 0：乐富购，1：宝谛乐
     */
    private Integer spuChannel;

    /**
     * 是否包邮，0：否，1：是
     */
    private Integer isFreepost;

    /**
     * 运费模板计价方式，0：按重量(KG)，1：按体积( m3)，2：按数量(件)
     */
    private Integer freightValuationWay;
    /**
     * 图文详情
     */
    private String productDes;
    /**
     * 主图
     */
    private String mainImg;
    /**
     * 是否多属性（0：否1：是）
     */
    private Integer isMultiAttr;
    /**
     * 是否多规格（0：否1：是）
     */
    private Integer isMultiSpec;
    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 商品录入来源，0：供应商系统，1：商品中心
     */
    private Integer inputSource;

    /**
     * 商品图片
     */
    private List<SpuImg> spuImgs;

    private List<ProductSkuInfo> skus;

    private String supplierName;//供应商名称

    private String poolCode;
}
