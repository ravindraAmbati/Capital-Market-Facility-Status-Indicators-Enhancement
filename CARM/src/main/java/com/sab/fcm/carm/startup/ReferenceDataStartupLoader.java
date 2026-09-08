package com.sab.fcm.carm.startup;

import com.sab.fcm.carm.cache.ReferenceDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ReferenceDataStartupLoader {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ReferenceDataStartupLoader.class);

    private final ReferenceDataCache cache;

    public ReferenceDataStartupLoader(ReferenceDataCache cache) {
        this.cache = cache;
    }

    @PostConstruct
    public void load() {
        LOGGER.info(
                "Loading Facility Capital Markers reference data during CARM startup");

        cache.get();

        LOGGER.info(
                "Facility Capital Markers reference data loaded successfully during CARM startup");
    }
}
