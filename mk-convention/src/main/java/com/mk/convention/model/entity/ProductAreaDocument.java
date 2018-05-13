package com.mk.convention.model.entity;

import com.mk.convention.utils.jd.DataEvent;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @program: lovego-jd
 * @description: 地址拉取
 * @author: Miaoxy
 * @create: 2018-05-09 11:47
 **/
@Data
@Document(indexName = ProductAreaDocument.INDEX, type = ProductAreaDocument.TYPE)
public class ProductAreaDocument implements DataEvent,java.io.Serializable{

    public static final String INDEX = "product_area";
    public static final String TYPE = "area";
    private static final long serialVersionUID = 2144680796145137212L;

    /**
     * dizhi
     */
    @Id
    @Field(type = FieldType.keyword)
    private String areaId;
    /**
     * 商品信息
     */
    private String baseArea ;
}
