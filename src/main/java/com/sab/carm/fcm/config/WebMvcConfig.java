package com.sab.carm.fcm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiAuditInterceptor apiAuditInterceptor;

    public WebMvcConfig(ApiAuditInterceptor apiAuditInterceptor) {
        this.apiAuditInterceptor = apiAuditInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAuditInterceptor)
                .addPathPatterns("/api/**");
    }
}
