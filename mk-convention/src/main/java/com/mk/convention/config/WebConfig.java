//package com.mk.convention.config;
//
//import java.util.Arrays;
//import java.util.Properties;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.aop.Advisor;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.aop.support.JdkRegexpMethodPointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.support.GenericConversionService;
//import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
//import org.springframework.web.context.request.RequestContextListener;
//import org.springframework.web.servlet.HandlerAdapter;
//import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import com.alibaba.druid.support.spring.stat.BeanTypeAutoProxyCreator;
//import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
//import com.stylefeng.guns.core.util.StringToDateConverter;
//import com.stylefeng.guns.core.xss.XssFilter;
//
///**
// * web 配置类
// *
// * @author fengshuonan
// * @date 2016年11月12日 下午5:03:32
// */
//@Configuration
//public class WebConfig {
//
//
//    /**
//     * druidServlet注册
//     */
//    @Bean
//    public ServletRegistrationBean druidServletRegistration() {
//        ServletRegistrationBean registration = new ServletRegistrationBean(new StatViewServlet());
//        registration.addUrlMappings("/druid/*");
//        return registration;
//    }
//
//    /**
//     * druid监控 配置URI拦截策略
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean druidStatFilter(){
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
//        //添加过滤规则.
//        filterRegistrationBean.addUrlPatterns("/*");
//        //添加不需要忽略的格式信息.
//        filterRegistrationBean.addInitParameter(
//                "exclusions","/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid,/druid/*");
//        //用于session监控页面的用户名显示 需要登录后主动将username注入到session里
//        filterRegistrationBean.addInitParameter("principalSessionName","username");
//        return filterRegistrationBean;
//    }
//
//    /**
//     * druid数据库连接池监控
//     */
//    @Bean
//    public DruidStatInterceptor druidStatInterceptor() {
//        return new DruidStatInterceptor();
//    }
//
//
//    @Bean
//    public JdkRegexpMethodPointcut druidStatPointcut(){
//        JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
//        String patterns = "com.stylefeng.guns.modular.*.service.*";
//        //可以set多个
//        druidStatPointcut.setPatterns(patterns);
//        return druidStatPointcut;
//    }
//
//    /**
//     * druid数据库连接池监控
//     */
//    @Bean
//    public BeanTypeAutoProxyCreator beanTypeAutoProxyCreator() {
//        BeanTypeAutoProxyCreator beanTypeAutoProxyCreator = new BeanTypeAutoProxyCreator();
//        beanTypeAutoProxyCreator.setTargetBeanType(DruidDataSource.class);
//        beanTypeAutoProxyCreator.setInterceptorNames("druidStatInterceptor");
//        return beanTypeAutoProxyCreator;
//    }
//
//    /**
//     * druid 为druidStatPointcut添加拦截
//     * @return
//     */
//    @Bean
//    public Advisor druidStatAdvisor() {
//        return new DefaultPointcutAdvisor(druidStatPointcut(), druidStatInterceptor());
//    }
//
//    /**
//     * xssFilter注册
//     */
//    @Bean
//    public FilterRegistrationBean xssFilterRegistration() {
//        XssFilter xssFilter = new XssFilter();
//        xssFilter.setUrlExclusion(Arrays.asList("/notice/update","/notice/add"));
//        FilterRegistrationBean registration = new FilterRegistrationBean(xssFilter);
//        registration.addUrlPatterns("/*");
//        return registration;
//    }
//
//    /**
//     * RequestContextListener注册
//     */
//    @Bean
//    public ServletListenerRegistrationBean<RequestContextListener> requestContextListenerRegistration() {
//        return new ServletListenerRegistrationBean<>(new RequestContextListener());
//    }
//
//
//}
