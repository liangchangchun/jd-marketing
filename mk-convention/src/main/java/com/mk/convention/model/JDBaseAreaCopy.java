package com.mk.convention.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class JDBaseAreaCopy implements Serializable{

    private static final long serialVersionUID = -8504707747353663165L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 地区全称
     */
    private String areaAllname;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 上级地区id
     */
    private Long parentId;

    /**
     * 地区类型(0-市 1-省 2-区(县) 3-乡(镇))
     */
    private String areaType;

    /**
     * 是否删除(0-已删除 1-未删除)
     */
    private Boolean isDelete;

    /**
     * 大区域ID
     */
    private Long bigAreaId;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long modifier;

    /**
     * 修改时间
     */
    private Date modifyTime;

}