package com.sab.fcm.carm.config;

import com.sab.fcm.carm.cache.ReferenceDataCache;
import com.sab.fcm.carm.notification.FailureNotificationService;
import com.sab.fcm.carm.retry.RetryExecutor;
import com.sab.fcm.carm.service.ReferenceDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@PropertySource("classpath:carm-fcm.properties")
@ComponentScan(basePackages = "com.sab.fcm.carm")
public class CarmFcmConfiguration {

    @Bean
    public RestTemplate fcmRestTemplate(CarmFcmProperties properties) {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(properties.getConnectTimeoutMs());
        factory.setReadTimeout(properties.getReadTimeoutMs());

        return new RestTemplate(factory);
    }

    @Bean(destroyMethod = "shutdown")
    public Executor fcmReferenceDataRefreshExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public RetryExecutor fcmRetryExecutor(CarmFcmProperties properties) {
        return new RetryExecutor(
                properties.getMaxAttempts(),
                properties.getInitialBackoffMs(),
                properties.getMaxBackoffMs(),
                properties.getRetryMultiplier());
    }

    @Bean
    public ReferenceDataCache referenceDataCache(
            ReferenceDataProvider provider,
            RetryExecutor retryExecutor,
            FailureNotificationService notificationService,
            Executor fcmReferenceDataRefreshExecutor,
            CarmFcmProperties properties) {

        return new ReferenceDataCache(
                provider,
                retryExecutor,
                notificationService,
                fcmReferenceDataRefreshExecutor,
                properties.getSoftTtlMs(),
                properties.getHardTtlMs());
    }
}
