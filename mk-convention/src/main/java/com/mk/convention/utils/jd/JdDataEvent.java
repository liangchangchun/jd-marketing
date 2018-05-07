package com.mk.convention.utils.jd;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.mk.convention.config.DataSource;

import lombok.Data;

@Data
public class JdDataEvent {
	private long id ;
	private DataEvent event ;
	private String command ;
	
	private DataSource dataSource;//jdbc
	
	private ElasticsearchCrudRepository esRes;
	
}
