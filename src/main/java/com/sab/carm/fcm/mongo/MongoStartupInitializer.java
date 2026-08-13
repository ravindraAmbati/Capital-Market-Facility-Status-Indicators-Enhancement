package com.sab.carm.fcm.mongo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MongoStartupInitializer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MongoStartupInitializer.class);

    private final MongoProperties mongoProperties;

    private final MongoCollectionInitializer mongoCollectionInitializer;

    public MongoStartupInitializer(
            MongoProperties mongoProperties,
            MongoCollectionInitializer mongoCollectionInitializer) {

        this.mongoProperties = mongoProperties;
        this.mongoCollectionInitializer = mongoCollectionInitializer;
    }

    @PostConstruct
    public void initialize() {

        LOGGER.info("------------------------------------------------------------");
        LOGGER.info("MongoDB Startup Initializer");
        LOGGER.info("Collection Initialization : {}",
                mongoProperties.isInitializeCollections());

        if (mongoProperties.isInitializeCollections()) {

            mongoCollectionInitializer.initialize();

        } else {

            LOGGER.info("MongoDB collection initialization is disabled.");

        }

        LOGGER.info("MongoDB Startup Initializer completed.");
        LOGGER.info("------------------------------------------------------------");
    }
}