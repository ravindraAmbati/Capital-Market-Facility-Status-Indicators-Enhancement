package com.sab.carm.fcm.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@DependsOn("mongoConnectionValidator")
public class MongoCollectionInitializer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    MongoCollectionInitializer.class);

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

            LOGGER.info(
                    "MongoDB collection initialization is disabled.");

            return;
        }

        for (MongoProperties.CollectionDefinition definition :
                mongoProperties.getDbCollectionNames().asList()) {

            createCollection(definition);
        }
    }

    private void createCollection(
            MongoProperties.CollectionDefinition definition) {

        if (definition == null
                || !definition.isCreateIfMissing()) {

            return;
        }

        String collectionName = definition.getName();

        if (collectionName == null
                || collectionName.trim().isEmpty()) {

            LOGGER.warn(
                    "Skipping MongoDB collection initialization because "
                            + "collection name is not configured.");

            return;
        }

        if (!mongoTemplate.collectionExists(collectionName)) {

            LOGGER.info(
                    "Creating MongoDB collection [{}]",
                    collectionName);

            mongoTemplate.createCollection(collectionName);

        } else {

            LOGGER.info(
                    "MongoDB collection [{}] already exists.",
                    collectionName);
        }
    }
}
