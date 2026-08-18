package com.sab.carm.fcm.carm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CarmReferenceDataStartup implements ApplicationRunner {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CarmReferenceDataStartup.class);

    private final CarmMaintenanceReferenceDataService service;

    public CarmReferenceDataStartup(
            CarmMaintenanceReferenceDataService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {

        LOGGER.info("Starting CARM reference-data synchronization.");

        service.refresh();

        LOGGER.info("CARM reference-data synchronization completed.");
    }
}