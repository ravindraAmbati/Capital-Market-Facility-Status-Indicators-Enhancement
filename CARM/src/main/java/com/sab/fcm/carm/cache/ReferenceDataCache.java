package com.sab.fcm.carm.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.sab.fcm.carm.notification.FailureNotificationService;
import com.sab.fcm.carm.retry.RetryExecutor;
import com.sab.fcm.carm.service.ReferenceDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

public class ReferenceDataCache {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ReferenceDataCache.class);

    private static final String CACHE_KEY = "REFERENCE_DATA";

    private final ReferenceDataProvider provider;
    private final RetryExecutor retryExecutor;
    private final FailureNotificationService notificationService;
    private final Executor refreshExecutor;
    private final long softTtlMs;
    private final long hardTtlMs;

    private final ConcurrentMap<String, CacheEntry> cache =
            new ConcurrentHashMap<String, CacheEntry>();

    private final ConcurrentMap<String, Object> locks =
            new ConcurrentHashMap<String, Object>();

    private final ConcurrentMap<String, Boolean> refreshInProgress =
            new ConcurrentHashMap<String, Boolean>();

    private final CacheMetrics metrics = new CacheMetrics();

    public ReferenceDataCache(
            ReferenceDataProvider provider,
            RetryExecutor retryExecutor,
            FailureNotificationService notificationService,
            Executor refreshExecutor,
            long softTtlMs,
            long hardTtlMs) {

        if (softTtlMs <= 0 || hardTtlMs <= 0 || hardTtlMs < softTtlMs) {
            throw new IllegalArgumentException(
                    "Cache TTL must satisfy 0 < softTtl <= hardTtl");
        }

        this.provider = provider;
        this.retryExecutor = retryExecutor;
        this.notificationService = notificationService;
        this.refreshExecutor = refreshExecutor;
        this.softTtlMs = softTtlMs;
        this.hardTtlMs = hardTtlMs;
    }

    public JsonNode get() {
        long now = System.currentTimeMillis();
        CacheEntry entry = cache.get(CACHE_KEY);

        if (entry == null) {
            metrics.miss();
            LOGGER.debug("FCM reference-data cache MISS");
            return loadSynchronously();
        }

        if (!entry.isSoftExpired(now)) {
            metrics.hit();
            LOGGER.debug("FCM reference-data cache HIT");
            return copy(entry.getValue());
        }

        if (!entry.isHardExpired(now)) {
            metrics.hit();
            LOGGER.debug("FCM reference-data cache SOFT-EXPIRED HIT");
            refreshAsync();
            return copy(entry.getValue());
        }

        metrics.miss();
        LOGGER.debug("FCM reference-data cache HARD-EXPIRED MISS");
        return loadAfterHardExpiry();
    }

    public void refreshNow() {
        Object lock = getLock();

        synchronized (lock) {
            try {
                JsonNode fresh = loadWithRetry();
                put(fresh);
                LOGGER.info("FCM reference data refreshed successfully");
            } catch (Exception e) {
                handleRefreshFailure(e);
            }
        }
    }

    public void invalidate() {
        cache.remove(CACHE_KEY);
        LOGGER.info("FCM reference-data cache invalidated");
    }

    public CacheMetrics getMetrics() {
        return metrics;
    }

    public boolean isLoaded() {
        return cache.containsKey(CACHE_KEY);
    }

    public JsonNode getCachedValueForTest() {
        CacheEntry entry = cache.get(CACHE_KEY);
        return entry == null ? null : copy(entry.getValue());
    }

    private JsonNode loadSynchronously() {
        Object lock = getLock();

        synchronized (lock) {
            CacheEntry existing = cache.get(CACHE_KEY);

            if (existing != null) {
                long now = System.currentTimeMillis();
                if (!existing.isHardExpired(now)) {
                    metrics.hit();
                    return copy(existing.getValue());
                }
            }

            try {
                JsonNode fresh = loadWithRetry();
                put(fresh);
                return copy(fresh);
            } catch (Exception e) {
                notificationService.notifyFailure(
                        "FCM reference-data initial load failed",
                        e);

                throw new IllegalStateException(
                        "Unable to load FCM reference data",
                        e);
            }
        }
    }

    private JsonNode loadAfterHardExpiry() {
        Object lock = getLock();

        synchronized (lock) {
            CacheEntry current = cache.get(CACHE_KEY);
            long now = System.currentTimeMillis();

            if (current != null && !current.isHardExpired(now)) {
                metrics.hit();
                return copy(current.getValue());
            }

            if (current != null &&
                    now < current.getNextRetryAllowedAt()) {

                LOGGER.warn(
                        "Returning last-known-good FCM reference data " +
                        "because a previous refresh failed");

                return copy(current.getValue());
            }

            try {
                JsonNode fresh = loadWithRetry();
                put(fresh);
                return copy(fresh);
            } catch (Exception e) {
                if (current != null) {
                    current.setNextRetryAllowedAt(
                            System.currentTimeMillis() + softTtlMs);

                    handleRefreshFailure(e);

                    return copy(current.getValue());
                }

                notificationService.notifyFailure(
                        "FCM reference-data refresh failed and no cached data exists",
                        e);

                throw new IllegalStateException(
                        "FCM reference data unavailable",
                        e);
            }
        }
    }

    private JsonNode loadWithRetry() throws Exception {
        return retryExecutor.execute(
                new java.util.concurrent.Callable<JsonNode>() {
                    @Override
                    public JsonNode call() {
                        return provider.loadReferenceData();
                    }
                });
    }

    private void refreshAsync() {
        if (refreshInProgress.putIfAbsent(
                CACHE_KEY,
                Boolean.TRUE) != null) {
            return;
        }

        try {
            refreshExecutor.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                refreshNow();
                            } finally {
                                refreshInProgress.remove(CACHE_KEY);
                            }
                        }
                    });
        } catch (RuntimeException e) {
            refreshInProgress.remove(CACHE_KEY);
            throw e;
        }
    }

    private void put(JsonNode value) {
        if (value == null) {
            throw new IllegalStateException(
                    "FCM returned null reference data");
        }

        long now = System.currentTimeMillis();

        cache.put(
                CACHE_KEY,
                new CacheEntry(
                        copy(value),
                        now,
                        now + softTtlMs,
                        now + hardTtlMs));
    }

    private void handleRefreshFailure(Exception e) {
        LOGGER.error(
                "FCM reference-data refresh failed",
                e);

        notificationService.notifyFailure(
                "FCM reference-data refresh failed",
                e);
    }

    private Object getLock() {
        Object existing = locks.get(CACHE_KEY);
        if (existing != null) {
            return existing;
        }

        Object created = new Object();
        Object previous = locks.putIfAbsent(CACHE_KEY, created);
        return previous == null ? created : previous;
    }

    private JsonNode copy(JsonNode value) {
        return value == null ? null : value.deepCopy();
    }
}
