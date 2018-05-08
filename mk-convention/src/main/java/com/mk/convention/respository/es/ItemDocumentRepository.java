package com.mk.convention.respository.es;


import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mk.convention.model.entity.ItemDocument;


/**
 * https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/
 * <p>
 * Created by lijingyao on 2018/1/15 11:03.
 */
public interface ItemDocumentRepository extends ElasticsearchRepository<ItemDocument, String> {


}
