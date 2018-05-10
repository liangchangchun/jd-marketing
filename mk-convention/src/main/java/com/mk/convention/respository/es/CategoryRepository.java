package com.mk.convention.respository.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.mk.convention.model.entity.CategoryDocument;

public interface CategoryRepository extends ElasticsearchCrudRepository<CategoryDocument, String>  {

}
