package com.mk.convention.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class JDApiConfig {

    @Value("${jd.openApi.host}")
    private String jdOpenApiHost;

    @Value("${jd.openApi.path}")
    private String path;

    @Value("${grantType}")
    private String grantType;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Value("${jd.userName}")
    private String userName;

    @Value("${jd.passWord}")
    private String passWord;
    
    
    private final String scope ="";
    
}
