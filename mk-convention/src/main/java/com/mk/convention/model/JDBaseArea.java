package com.mk.convention.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
//@Entity
//@Table(name = "jd_base_area")
public class JDBaseArea implements Serializable{

    private static final long serialVersionUID = -8504707747353663165L;

    /**
     * 主键ID
     */
   // @NotNull
   // @Column(name = "id", length = 20)
   // @GeneratedValue(strategy=GenerationType.AUTO) 
    private Long id;

    /**
     * 地区全称
     */
   // @Column(name = "area_allname", length = 200)
    private String areaAllname;

    /**
     * 地区名称
     */
   // @Column(name = "area_name", length = 50)
    private String areaName;

    /**
     * 地区编码
     */
   // @Column(name = "area_code", length = 50)
    private String areaCode;

    /**
     * 上级地区id
     */
   // @Column(name = "parent_id", length = 20)
    private Long parentId;

    /**
     * 地区类型(0-市 1-省 2-区(县) 3-乡(镇))
     */
   // @Column(name = "area_type", length = 2)
    private String areaType;

    /**
     * 是否删除(0-已删除 1-未删除)
     */
   // @Column(name = "is_delete", length = 1)
    private Boolean isDelete;

    /**
     * 大区域ID
     */
   // @Column(name = "big_area_id", length = 5)
    private Long bigAreaId;

    /**
     * 创建人
     */
   // @Column(name = "creator", length = 20)
    private Long creator;

    /**
     * 创建时间
     */
   // @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
   // @Column(name = "modifier", length = 20)
    private Long modifier;

    /**
     * 修改时间
     */
    //@Column(name = "modify_time")
    private Date modifyTime;

}