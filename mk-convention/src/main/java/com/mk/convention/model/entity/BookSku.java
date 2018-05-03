package com.mk.convention.model.entity;

import lombok.Data;

/**
 * 京东 图书类sku
 *
 * @Description:
 */
@Data
public class BookSku {
    private Long sku;
    private String skuType;
    private String Author;
    private String Editer;
    private String Transfer;
    private String Drawer;
    private String Proofreader;
    private Integer ISBN;
    private String publishers;
    private String Sheet;
    private Integer Pages;
    private String Package;
    private String PublishTime;
    private String BatchNo;
    private String PrIntegerTime;
    private String PrIntegerNo;
    private Integer PackNum;
    private String Language;
    private String Papers;
    private String Brand;
    private String comments;
    private String image;
    private String contentDesc;
    private String relatedProducts;
    private String editerDesc;
    private String catalogue;
    private String bookAbstract;
    private String authorDesc;
    private String Integerroduction;

/*
    {"result":
        {
            "sku" : 商品编号,
                "name": "商品名称"，
            "skuType" : "book",   // 可以使用这个字段区分是图书还是音像
                "Author" : "著者",
                "Editer" : "编者",
                "Transfer" : "译者",
                "Drawer" : "绘者",
                "Proofreader" : "校对",
                "ISBN" : ISBN,
                "Publishers" : "出版社",
                "Sheet" : "开本",
                "Pages" : 页数,
                "Package"："包装",
                "PublishTime"："出版时间",
                "BatchNo"："版次",
                "PrintTime"："印刷时间",
                "PrintNo"："印次",
                "PackNum"："套装数量",
                "Language"："正文语言",
                "Papers"："用纸",
                "Brand"："品牌",
                "comments"："媒体评论",
                "image"："插图",
                "contentDesc"："内容摘要",
                "relatedProducts"："产品描述",
                "editerDesc"："编辑推荐",
                "catalogue"："目录",
                "bookAbstract"："精彩摘要",
                "authorDesc"："作者简介",
                "introduction"："前言"
        }
    }
*/

}
