package com.sab.carm.fcm.health;

import com.mongodb.client.MongoClient;
import com.sab.carm.fcm.mongo.MongoProperties;
import org.bson.Document;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    private final MongoClient mongoClient;

    private final MongoProperties mongoProperties;

    public ApplicationHealthIndicator(
            MongoClient mongoClient,
            MongoProperties mongoProperties) {

        this.mongoClient = mongoClient;
        this.mongoProperties = mongoProperties;
    }

    @Override
    public Health health() {

        Health.Builder builder = Health.up();

        builder.withDetail("application", "UP");

        /*
         * MongoDB Health
         */
        if (!mongoProperties.isValidateConnection()) {

            builder.withDetail(
                    "mongodb",
                    "Validation Disabled");

            return builder.build();
        }

        try {

            mongoClient
                    .getDatabase("admin")
                    .runCommand(new Document("ping", 1));

            builder.withDetail(
                    "mongodb",
                    "UP");

        } catch (Exception ex) {

            builder.withDetail(
                    "mongodb",
                    "DOWN");

            builder.withDetail(
                    "mongodbError",
                    ex.getMessage());

            /*
             * We intentionally DO NOT return Health.down().
             *
             * MongoDB may be temporarily unavailable during
             * development or maintenance windows.
             */
        }

        return builder.build();
    }
}