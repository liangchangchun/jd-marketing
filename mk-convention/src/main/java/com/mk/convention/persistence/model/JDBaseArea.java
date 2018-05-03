package com.mk.convention.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "jd_base_area")
public class JDBaseArea implements Serializable{

    private static final long serialVersionUID = -8504707747353663165L;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;

    /**
     * 区域ID
     */
    @NotNull
    @Column(name = "area_id", length = 20)
    private Long areaId;

    /**
     * 地区全称
     */
    @Column(name = "area_allname", length = 200)
    private String areaAllname;

    /**
     * 地区名称
     */
    @Column(name = "area_name", length = 50)
    private String areaName;

    /**
     * 地区编码
     */
    @Column(name = "area_code", length = 50)
    private String areaCode;

    /**
     * 上级地区id
     */
    @Column(name = "parent_id", length = 20)
    private Long parentId;

    /**
     * 地区类型(0-市 1-省 2-区(县) 3-乡(镇))
     */
    @Column(name = "area_type", length = 2)
    private String areaType;

    /**
     * 是否删除(0-已删除 1-未删除)
     */
    @Column(name = "is_delete")
    private Boolean isDelete;

    /**
     * 大区域ID
     */
    @Column(name = "big_area_id", length = 5)
    private Long bigAreaId;

    /**
     * 创建人
     */
    @Column(name = "creator", length = 20)
    private Long creator;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "modifier", length = 20)
    private Long modifier;

    /**
     * 修改时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "modify_time")
    private Date modifyTime;



    public JDBaseArea() {

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaAllname() {
        return areaAllname;
    }

    public void setAreaAllname(String areaAllname) {
        this.areaAllname = areaAllname;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Long getBigAreaId() {
        return bigAreaId;
    }

    public void setBigAreaId(Long bigAreaId) {
        this.bigAreaId = bigAreaId;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}