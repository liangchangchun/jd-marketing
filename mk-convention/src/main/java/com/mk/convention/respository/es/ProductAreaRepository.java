package com.mk.convention.respository.es;

import com.mk.convention.model.entity.ProductAreaDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * @program: lovego-jd
 * @description: 地址存储
 * @author: Miaoxy
 * @create: 2018-05-09 13:26
 **/
public interface ProductAreaRepository extends ElasticsearchCrudRepository<ProductAreaDocument, String> {
}
