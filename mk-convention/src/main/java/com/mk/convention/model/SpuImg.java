package com.mk.convention.model;

import lombok.Data;

@Data
public class SpuImg {
    /**
     * 排序指数
     */
    private Integer sort;

    /**
     * 是否主图（0：否1：是）
     */
    private Integer isMain;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 图片路径
     */
    private String url;
}
