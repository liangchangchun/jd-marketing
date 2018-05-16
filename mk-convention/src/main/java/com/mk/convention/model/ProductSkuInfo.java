package com.mk.convention.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductSkuInfo {

    /**
     * sku码,新增商品不传
     */
    private String skuCode;

    /**
     *SPU名称
     */
    private String spuName;

    /**
     * 货号
     */
    private String artNo;

    /**
     * 条形码
     */
    private String barCode;
    /**
     * 进价
     */
    private BigDecimal costPrice;
    /**
     * 售价
     */
    private BigDecimal salePrice;
    /**
     * 红包抵扣值
     */
    private BigDecimal redPackage;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 市场价
     */
    private BigDecimal inputTax;
    /**
     * 市场价
     */
    private BigDecimal outputTax;
    /**
     * 长
     */
    private BigDecimal length;
    /**
     * 宽
     */
    private BigDecimal width;
    /**
     * 高
     */
    private BigDecimal height;
    /**
     * 重量
     */
    private BigDecimal roughWeight;
    /**
     * 运营成本
     */
    private BigDecimal operatingCosts;
    /**
     * 商品池编码
     */
    private String poolCode;
    /**
     * 资金池比例
     */
    private BigDecimal poolRoportion;
    /**
     * 商品毛利率
     */
    private Integer isRedpacket;

    //组合商品中组合商品的售卖基数
    private Integer skuNum;

    private BigDecimal redpacketPrice;

    private Long shelvesTimeStamp;

    private Long shelfTimeStamp;

    private String shelfReason;

    /**
     * 主图
     */
    private String mainImg;

    /**
     * 是否主商品（0：否1：是）
     */
    private Integer isMain;


    /**
     * 多个规格值拼接的字符串 如：黄色,L
     */
    private String specValues;


    private Integer oldStatus;

    /**
     * 规格值
     */
    private String specsValues;
}
