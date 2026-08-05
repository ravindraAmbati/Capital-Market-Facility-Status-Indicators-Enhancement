package com.company.application.config;

import com.company.application.mongo.MongoProperties;
import com.company.application.mongo.MongoServerAddressParser;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.StringUtils;

/**
 * Enables MongoDB repositories and automatic index creation.
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.company.application.repository")
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig {

    @Bean
    public MongoClient mongoClient(MongoProperties properties, MongoServerAddressParser addressParser) {
        MongoClientSettings.Builder builder = MongoClientSettings.builder()
                .applyToClusterSettings(settings -> settings.hosts(addressParser.parse(properties.getServerAddresses())))
                .applyToSocketSettings(settings -> settings
                        .connectTimeout(properties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                        .readTimeout(properties.getSocketTimeout(), TimeUnit.MILLISECONDS))
                .applyToSslSettings(settings -> settings
                        .enabled(properties.isSslEnabled())
                        .invalidHostNameAllowed(properties.isSslInvalidHostnameAllowed()));
        if (StringUtils.hasText(properties.getUsername())) {
            builder.credential(MongoCredential.createCredential(properties.getUsername(),
                    properties.getAuthenticationDatabase(), properties.getPassword().toCharArray()));
        }
        return MongoClients.create(builder.build());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient, MongoProperties properties) {
        return new MongoTemplate(mongoClient, properties.getDatabase());
    }
}
