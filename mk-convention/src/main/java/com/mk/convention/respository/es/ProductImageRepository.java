package com.mk.convention.respository.es;

import com.mk.convention.model.entity.ProductImageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ProductImageRepository extends ElasticsearchCrudRepository<ProductImageDocument, String> {
}
