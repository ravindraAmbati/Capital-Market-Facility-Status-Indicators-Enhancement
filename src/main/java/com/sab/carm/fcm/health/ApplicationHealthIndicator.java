package com.sab.carm.fcm.health;

import com.sab.carm.fcm.mongo.MongoProperties;
import com.sab.carm.fcm.mongo.MongoStartupStatus;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Provides a simple application health contribution.
 */
@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    private final MongoProperties mongoProperties;
    private final MongoStartupStatus mongoStartupStatus;

    public ApplicationHealthIndicator(MongoProperties mongoProperties, MongoStartupStatus mongoStartupStatus) {
        this.mongoProperties = mongoProperties;
        this.mongoStartupStatus = mongoStartupStatus;
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("framework", "ready")
                .withDetail("databaseName", mongoProperties.getDatabase())
                .withDetail("collectionsInitialized", mongoStartupStatus.getInitializedCollections())
                .withDetail("referenceDataVersion", mongoStartupStatus.getReferenceDataVersion())
                .withDetail("startupStatus", mongoStartupStatus.isSuccessful() ? "SUCCESS" : "STARTING")
                .build();
    }
}
