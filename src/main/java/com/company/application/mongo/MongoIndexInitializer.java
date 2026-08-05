package com.company.application.mongo;

import com.company.application.exception.MongoInfrastructureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

/**
 * Creates required MongoDB indexes idempotently.
 */
@Component
public class MongoIndexInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoIndexInitializer.class);

    public void initialize(MongoTemplate mongoTemplate, MongoProperties properties) {
        try {
            String versionCollection = properties.getDbCollectionNames().get("applicationVersion");
            String auditCollection = properties.getDbCollectionNames().get("auditTrail");
            ensureIndex(mongoTemplate, versionCollection, new Index().on("version", Sort.Direction.ASC).unique().named("version"));
            ensureIndex(mongoTemplate, auditCollection, new Index().on("eventType", Sort.Direction.ASC).named("eventType"));
            ensureIndex(mongoTemplate, auditCollection, new Index().on("username", Sort.Direction.ASC).named("username"));
            LOGGER.info("MongoDB indexes initialized");
        } catch (RuntimeException ex) {
            throw new MongoInfrastructureException("MongoDB index initialization failed", ex);
        }
    }

    private void ensureIndex(MongoTemplate mongoTemplate, String collectionName, Index index) {
        try {
            mongoTemplate.indexOps(collectionName).ensureIndex(index);
        } catch (UncategorizedMongoDbException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("IndexOptionsConflict")) {
                LOGGER.info("MongoDB index already exists with compatible definition collection={}", collectionName);
                return;
            }
            throw ex;
        }
    }
}
