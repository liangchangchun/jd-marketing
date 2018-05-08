package com.mk.convention.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.mk.convention.utils.jd.DataEvent;

import lombok.Data;

@Data
@Document(indexName = ProductDetailDocument.INDEX, type = ProductDetailDocument.TYPE)
public class ProductDetailDocument  implements DataEvent,java.io.Serializable{
	private static final long serialVersionUID = -8776170337442283017L;
	
	public static final String INDEX = "product";
    public static final String TYPE = "detail";

    /**
     * 商品SkuId
     */
    @Id
    @Field(type = FieldType.keyword)
    private String skuId;
    /**
     * 商品信息
     */
    private String info ;
}
