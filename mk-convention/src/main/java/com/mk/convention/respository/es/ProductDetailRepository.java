package com.mk.convention.respository.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.mk.convention.model.entity.ProductDetailDocument;


public interface ProductDetailRepository extends ElasticsearchCrudRepository<ProductDetailDocument, String> {

}
