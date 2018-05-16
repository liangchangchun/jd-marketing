package com.mk.convention.model.entity;

import com.mk.convention.utils.jd.DataEvent;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @program: lovego-jd
 * @description: 商品图片
 * @author: Miaoxy
 * @create: 2018-05-08 16:19
 **/
@Data
@Document(indexName = ProductImageDocument.INDEX, type = ProductImageDocument.TYPE)
public class ProductImageDocument implements DataEvent,java.io.Serializable{

    public static final String INDEX = "product_image";
    public static final String TYPE = "image";
    private static final long serialVersionUID = 2638299929276103817L;

    /**
     * 商品SkuId
     */
    @Id
    private String skuId;
    /**
     * 商品信息
     */
    private String image ;
}
