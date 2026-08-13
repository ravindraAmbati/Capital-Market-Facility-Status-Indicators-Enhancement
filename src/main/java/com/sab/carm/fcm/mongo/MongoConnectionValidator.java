package com.sab.carm.fcm.mongo;

import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MongoConnectionValidator {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MongoConnectionValidator.class);

    private final MongoClient mongoClient;

    private final MongoProperties mongoProperties;

    public MongoConnectionValidator(
            MongoClient mongoClient,
            MongoProperties mongoProperties) {

        this.mongoClient = mongoClient;
        this.mongoProperties = mongoProperties;
    }

    public void validate() {

        if (!mongoProperties.isValidateConnection()) {

            LOGGER.info("MongoDB connection validation is disabled.");

            return;
        }

        LOGGER.info("Validating MongoDB connection...");

        try {

            mongoClient
                    .getDatabase("admin")
                    .runCommand(new Document("ping", 1));

            LOGGER.info("MongoDB connection validated successfully.");

        } catch (Exception ex) {

            LOGGER.error("MongoDB connection validation failed.", ex);

            throw new IllegalStateException(
                    "Unable to connect to MongoDB.",
                    ex);
        }
    }
}