package com.mk.convention.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 */
@Data
public class OtherSku {
    private Long sku;
    private BigDecimal weight;
    private String imagePath;
    private Integer state;
    private String brandName;
    private String name;
    private String productArea;
    private String upc;
    private String saleUnit;
    private String category;
    private String eleGift;
    private String introduction;
    private String param;
    private String wareQD;


}
