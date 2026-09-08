package com.sab.fcm.carm.scheduler;

import com.sab.fcm.carm.cache.CacheMetrics;
import com.sab.fcm.carm.cache.ReferenceDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheMetricsLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CacheMetricsLogger.class);

    private final ReferenceDataCache cache;

    public CacheMetricsLogger(ReferenceDataCache cache) {
        this.cache = cache;
    }

    @Scheduled(fixedDelay = 300000)
    public void logMetrics() {

        CacheMetrics metrics = cache.getMetrics();

        LOGGER.info(
                "FCM Reference Data Cache Metrics - hits={}, misses={}, " +
                "hitRatio={}%, missRatio={}%",
                metrics.getHits(),
                metrics.getMisses(),
                format(metrics.getHitRatio() * 100.0),
                format(metrics.getMissRatio() * 100.0));
    }

    private String format(double value) {
        return String.format(java.util.Locale.ENGLISH, "%.4f", value);
    }
}
