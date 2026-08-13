package com.sab.carm.fcm.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * MongoDB Index Initializer.
 *
 * Index creation is controlled using:
 *
 * mongodb.initializeIndexes=true|false
 *
 * Current Status:
 * ---------------
 * Index creation is intentionally disabled until the MongoDB
 * database is provisioned.
 *
 * Future Release:
 * ---------------
 * This class will create all required indexes after ensuring
 * that the collections exist.
 */
@Component
public class MongoIndexInitializer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MongoIndexInitializer.class);

    private final MongoProperties mongoProperties;

    public MongoIndexInitializer(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    public void initialize() {

        if (!mongoProperties.isInitializeIndexes()) {

            LOGGER.info("MongoDB index initialization is disabled.");

            return;
        }

        LOGGER.info("MongoDB index initialization started.");

        /*
         * Future implementation:
         *
         * - Facility Capital Markers Indexes
         * - Decision History Indexes
         * - Report Indexes
         * - Audit Log Indexes
         * - Reference Data Indexes
         */

        LOGGER.info("MongoDB index initialization completed.");
    }
}