package com.sab.carm.fcm.mongo;

import com.sab.carm.fcm.exception.MongoInfrastructureException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Validates MongoDB connectivity during startup.
 */
@Component
public class MongoConnectionValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConnectionValidator.class);

    public void validate(MongoTemplate mongoTemplate, MongoProperties properties) {
        try {
            mongoTemplate.getDb().runCommand(new Document("ping", 1));
            mongoTemplate.getDb().listCollectionNames().first();
            LOGGER.info("MongoDB connectivity validated database={}", properties.getDatabase());
        } catch (RuntimeException ex) {
            throw new MongoInfrastructureException("MongoDB startup validation failed", ex);
        }
    }
}
