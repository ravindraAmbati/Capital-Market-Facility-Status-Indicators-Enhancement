package com.sab.fcm.carm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("carmFcmProperties")
public class CarmFcmProperties {

    @Value("${carm.fcm.base-url}")
    private String baseUrl;

    @Value("${carm.fcm.username}")
    private String username;

    @Value("${carm.fcm.password}")
    private String encryptedPassword;

    @Value("${carm.fcm.jasypt.password:}")
    private String jasyptPassword;

    @Value("${carm.fcm.jasypt.algorithm:PBEWithMD5AndDES}")
    private String jasyptAlgorithm;

    @Value("${carm.fcm.admin-api-prefix:/api/admin}")
    private String adminApiPrefix;

    @Value("${carm.fcm.reference-data.endpoints}")
    private String referenceDataEndpoints;

    @Value("${carm.fcm.connect-timeout-ms:5000}")
    private int connectTimeoutMs;

    @Value("${carm.fcm.read-timeout-ms:15000}")
    private int readTimeoutMs;

    @Value("${carm.fcm.cache.refresh-interval-ms:3600000}")
    private long refreshIntervalMs;

    @Value("${carm.fcm.cache.soft-ttl-ms:3300000}")
    private long softTtlMs;

    @Value("${carm.fcm.cache.hard-ttl-ms:7200000}")
    private long hardTtlMs;

    @Value("${carm.fcm.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${carm.fcm.retry.initial-backoff-ms:500}")
    private long initialBackoffMs;

    @Value("${carm.fcm.retry.max-backoff-ms:5000}")
    private long maxBackoffMs;

    @Value("${carm.fcm.retry.multiplier:2.0}")
    private double retryMultiplier;

    @Value("${carm.fcm.notification.enabled:true}")
    private boolean notificationEnabled;

    @Value("${carm.fcm.notification.from:}")
    private String notificationFrom;

    @Value("${carm.fcm.notification.to:}")
    private String notificationTo;

    public String getBaseUrl() { return baseUrl; }
    public String getUsername() { return username; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public String getJasyptPassword() { return jasyptPassword; }
    public String getJasyptAlgorithm() { return jasyptAlgorithm; }
    public String getAdminApiPrefix() { return adminApiPrefix; }
    public String getReferenceDataEndpoints() { return referenceDataEndpoints; }
    public int getConnectTimeoutMs() { return connectTimeoutMs; }
    public int getReadTimeoutMs() { return readTimeoutMs; }
    public long getRefreshIntervalMs() { return refreshIntervalMs; }
    public long getSoftTtlMs() { return softTtlMs; }
    public long getHardTtlMs() { return hardTtlMs; }
    public int getMaxAttempts() { return maxAttempts; }
    public long getInitialBackoffMs() { return initialBackoffMs; }
    public long getMaxBackoffMs() { return maxBackoffMs; }
    public double getRetryMultiplier() { return retryMultiplier; }
    public boolean isNotificationEnabled() { return notificationEnabled; }
    public String getNotificationFrom() { return notificationFrom; }
    public String getNotificationTo() { return notificationTo; }
}
