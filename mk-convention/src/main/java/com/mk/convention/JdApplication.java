package com.mk.convention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

//注入es 库
@EnableSearch
@SpringBootApplication
public class JdApplication  {
	//日志
	private static Logger logger = LoggerFactory.getLogger(JdApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JdApplication.class, args);
		logger.info("\r\n\n ================= Mk Convention  Server Boot Successfully ================= \r\n\n");
	}

}
