package com.mk.convention.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.mk.convention.utils.jd.DataEvent;

import lombok.Data;

@Data
@Document(indexName = CategoryDocument.INDEX, type = CategoryDocument.TYPE)
public class CategoryDocument implements DataEvent,java.io.Serializable{

	private static final long serialVersionUID = 2205025352574794113L;
	public static final String INDEX = "product_category";
    public static final String TYPE = "category";
    
    /**
     * 商品SkuId
     */
    @Id
    private String skuId;
    /**
     * 商品池列表
     */
    private String categoryId ;
}
