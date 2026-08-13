package com.sab.carm.fcm.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Coordinates MongoDB startup validation, collections, indexes, and reference data.
 */
@Component
public class MongoStartupInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoStartupInitializer.class);

    private final MongoTemplate mongoTemplate;
    private final MongoProperties properties;
    private final MongoConnectionValidator validator;
    private final MongoCollectionInitializer collectionInitializer;
    private final MongoIndexInitializer indexInitializer;
    private final ReferenceDataLoader referenceDataLoader;
    private final MongoStartupStatus status;

    public MongoStartupInitializer(MongoTemplate mongoTemplate, MongoProperties properties,
            MongoConnectionValidator validator, MongoCollectionInitializer collectionInitializer,
            MongoIndexInitializer indexInitializer, ReferenceDataLoader referenceDataLoader, MongoStartupStatus status) {
        this.mongoTemplate = mongoTemplate;
        this.properties = properties;
        this.validator = validator;
        this.collectionInitializer = collectionInitializer;
        this.indexInitializer = indexInitializer;
        this.referenceDataLoader = referenceDataLoader;
        this.status = status;
    }

    @Override
    public void run(ApplicationArguments args) {
        long started = System.currentTimeMillis();
        LOGGER.info("MongoDB startup initialization started database={}", properties.getDatabase());
        validator.validate(mongoTemplate, properties);
        collectionInitializer.initialize(mongoTemplate, properties, status);
        indexInitializer.initialize(mongoTemplate, properties);
        referenceDataLoader.load(mongoTemplate, status);
        status.setSuccessful(true);
        LOGGER.info("MongoDB startup initialization completed durationMs={}", System.currentTimeMillis() - started);
    }
}
