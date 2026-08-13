package com.sab.carm.fcm.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.sab.carm.fcm.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * Enables MongoDB repositories and automatic index creation.
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig {

    @Bean
    public MongoClient mongoClient(MongoProperties properties) {

        String connectionString =
                buildConnectionString(properties);

        ConnectionString cs =
                new ConnectionString(connectionString);

        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(cs)
                        .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(
            MongoClient client,
            MongoProperties properties) {

        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        client,
                        properties.getDatabase()));
    }

    private String buildConnectionString(
            MongoProperties properties) {

        return String.format(
                "mongodb://%s:%s@%s/%s?authSource=%s&replicaSet=%s&ssl=%s",
                properties.getUsername(),
                properties.getPassword(),
                String.join(",", properties.getHosts()),
                properties.getDatabase(),
                properties.getAuthenticationDatabase(),
                properties.getReplicaSet(),
                properties.isSslEnabled());
    }
}