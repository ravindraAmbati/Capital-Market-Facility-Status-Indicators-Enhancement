package com.sab.fcm.carm.scheduler;

import com.sab.fcm.carm.cache.ReferenceDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReferenceDataRefreshScheduler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ReferenceDataRefreshScheduler.class);

    private final ReferenceDataCache cache;

    public ReferenceDataRefreshScheduler(ReferenceDataCache cache) {
        this.cache = cache;
    }

    @Scheduled(
            fixedDelayString =
                    "#{@carmFcmProperties.refreshIntervalMs}")
    public void refresh() {

        LOGGER.info("Starting scheduled FCM reference-data refresh");

        cache.refreshNow();

        LOGGER.info("Completed scheduled FCM reference-data refresh");
    }
}
