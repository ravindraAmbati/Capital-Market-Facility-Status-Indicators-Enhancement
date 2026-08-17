package com.sab.carm.fcm.carm;

import java.util.List;
import java.util.Map;
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

    private final CarmReferenceDataService service;

    public CarmReferenceDataStartup(
            CarmReferenceDataService service) {

        this.service = service;
    }

    @Override
    public void run(
            ApplicationArguments args) {

        LOGGER.info(
                "Starting CARM reference-data fetch.");

        Map<String, List<Map<String, Object>>> data =
                service.fetchConfiguredReferenceData();

        LOGGER.info(
                "CARM reference-data fetch completed. "
                        + "successfulTableCount={}",
                data.size());
    }
}