package com.mk.convention.model.entity;

import lombok.Data;

/**
 * @program: lovego-jd
 * @description: 地址
 * @author: Miaoxy
 * @create: 2018-05-09 10:48
 **/
@Data
public class BaseArea {
    private String id;

    private String areaName;

    private String parentId;
}
