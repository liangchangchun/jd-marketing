package com.mk.convention.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
@Data
@Entity
@Table(name = "product_price_info")
public class ProductPriceInfo implements Serializable {

//    "marketPrice": 3222.0000,
//            "nakedPrice": 1281.20,
//            "price": 1499.00,
//            "taxPrice": 217.80,
//            "tax": 17,
//            "jdPrice": 1699.00,
//            "skuId": 2967929
@Id
@NotNull
@Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "market_price")
    private BigDecimal marketPrice;

    @Column(name = "naked_price")
    private BigDecimal nakedPrice;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "tax_price")
    private BigDecimal taxPrice;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "jd_price")
    private BigDecimal jdPrice;
}