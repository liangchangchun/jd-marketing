package com.mk.convention.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductPriceInfo {
    private Long id;

    private Long skuId;

    private BigDecimal marketprice;

    private BigDecimal nakedprice;

    private BigDecimal price;

    private BigDecimal taxprice;

    private BigDecimal tax;

    private BigDecimal jdprice;
}