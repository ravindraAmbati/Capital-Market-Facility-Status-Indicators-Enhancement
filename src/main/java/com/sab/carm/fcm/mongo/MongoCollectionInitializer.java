package com.sab.carm.fcm.mongo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoCollectionInitializer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MongoCollectionInitializer.class);

    private final MongoTemplate mongoTemplate;

    private final MongoProperties mongoProperties;

    public MongoCollectionInitializer(
            MongoTemplate mongoTemplate,
            MongoProperties mongoProperties) {

        this.mongoTemplate = mongoTemplate;
        this.mongoProperties = mongoProperties;
    }

    @PostConstruct
    public void initialize() {

        if (!mongoProperties.isInitializeCollections()) {

            LOGGER.info("MongoDB collection initialization is disabled.");

            return;
        }

        createCollection(
                mongoProperties.getDbCollectionNames()
                        .getFacilityCapitalMarkers());

        createCollection(
                mongoProperties.getDbCollectionNames()
                        .getFacilityCapitalMarkersDecisionHistory());

        createCollection(
                mongoProperties.getDbCollectionNames()
                        .getCreditApplicationCapitalMarkersReport());

        createCollection(
                mongoProperties.getDbCollectionNames()
                        .getApplicationAuditLog());

        createCollection(
                mongoProperties.getDbCollectionNames()
                        .getReferenceDataMappings());
    }

    private void createCollection(String collectionName) {

        if (!mongoTemplate.collectionExists(collectionName)) {

            LOGGER.info("Creating MongoDB collection [{}]", collectionName);

            mongoTemplate.createCollection(collectionName);

        } else {

            LOGGER.info("MongoDB collection [{}] already exists.", collectionName);
        }
    }
}