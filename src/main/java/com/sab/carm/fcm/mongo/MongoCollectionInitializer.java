package com.sab.carm.fcm.mongo;

import com.sab.carm.fcm.exception.MongoInfrastructureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Creates configured collections idempotently.
 */
@Component
public class MongoCollectionInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoCollectionInitializer.class);

    public void initialize(MongoTemplate mongoTemplate, MongoProperties properties, MongoStartupStatus status) {
        try {
            for (String collectionName : properties.getDbCollectionNames().values()) {
                if (!mongoTemplate.collectionExists(collectionName)) {
                    mongoTemplate.createCollection(collectionName);
                    LOGGER.info("MongoDB collection created collection={}", collectionName);
                } else {
                    LOGGER.info("MongoDB collection exists collection={}", collectionName);
                }
                status.getInitializedCollections().add(collectionName);
            }
        } catch (RuntimeException ex) {
            throw new MongoInfrastructureException("MongoDB collection initialization failed", ex);
        }
    }
}
