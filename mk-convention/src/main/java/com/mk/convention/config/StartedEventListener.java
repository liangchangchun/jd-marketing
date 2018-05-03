package com.mk.convention.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.mk.convention.config.property.JDApiConfig;
import com.mk.convention.service.HttpService;
import com.mk.convention.utils.SpringUtil;
import com.mk.convention.utils.jd.JDHttpTool;
import com.mk.convention.utils.jd.JdTransformTool;

@Component
public class StartedEventListener implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	JDApiConfig jDApiConfig;
	@Autowired
    HttpService  commonHttpService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(SpringUtil.getApplicationContext() == null){//注入上下文对象
			SpringUtil.setApplicationContext(event.getApplicationContext());
    	}
		JDHttpTool.getInstance().setConfig(jDApiConfig);//初始化京东请求工具
		JDHttpTool.getInstance().setCommonHttpService(commonHttpService);
		JdTransformTool.dataSource = new DataSource();
	}
	
}
