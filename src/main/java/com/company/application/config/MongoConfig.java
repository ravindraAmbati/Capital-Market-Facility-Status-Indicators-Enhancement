package com.company.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Enables MongoDB repositories and automatic index creation.
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.company.application.repository")
public class MongoConfig {
}
