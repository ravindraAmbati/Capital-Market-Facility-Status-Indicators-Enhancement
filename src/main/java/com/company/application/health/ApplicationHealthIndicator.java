package com.company.application.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Provides a simple application health contribution.
 */
@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.up().withDetail("framework", "ready").build();
    }
}
