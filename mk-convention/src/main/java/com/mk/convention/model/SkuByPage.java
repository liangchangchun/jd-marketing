package com.mk.convention.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: 邓双雄
 * @Descritioon:
 * @Date: 2018/4/28 17:56
 **/
@Data
public class SkuByPage {

    private Integer pageCount;

    private List<Long> skuIds;

}
