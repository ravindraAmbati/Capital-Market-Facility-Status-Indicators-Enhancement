package com.sab.carm.fcm.carm;

import com.sab.carm.fcm.service.CarmMaintenanceSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CarmReferenceDataStartup
        implements ApplicationRunner {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    CarmReferenceDataStartup.class);

    private final CarmMaintenanceSyncService syncService;

    public CarmReferenceDataStartup(
            CarmMaintenanceSyncService syncService) {

        this.syncService = syncService;
    }

    @Override
    public void run(ApplicationArguments args) {

        LOGGER.info(
                "Starting CARM reference-data synchronization.");

        syncService.syncAll();

        LOGGER.info(
                "CARM reference-data synchronization completed.");
    }
}