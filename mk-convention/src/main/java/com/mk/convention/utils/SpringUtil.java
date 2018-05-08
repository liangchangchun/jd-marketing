package com.mk.convention.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtil {
	 private static ApplicationContext applicationContext = null;
	// 非@import显式注入，@Component是必须的，且该类必须与main同包或子包
	    // 若非同包或子包，则需手动import 注入，有没有@Component都一样
	    // 可复制到Test同包测试

	    public static void setContext(ApplicationContext applicationContext) {
	        if(SpringUtil.applicationContext == null){
	            SpringUtil.applicationContext  = applicationContext;
	        }
	  
	    }

	    //获取applicationContext
	    public static ApplicationContext getContext() {
	        return applicationContext;
	    }

	    //通过name获取 Bean.
	    public static Object getBean(String name){
	        return getContext().getBean(name);

	    }

	    //通过class获取Bean.
	    public static <T> T getBean(Class<T> clazz){
	        return getContext().getBean(clazz);
	    }

	    //通过name,以及Clazz返回指定的Bean
	    public static <T> T getBean(String name,Class<T> clazz){
	        return getContext().getBean(name, clazz);
	    }
}
