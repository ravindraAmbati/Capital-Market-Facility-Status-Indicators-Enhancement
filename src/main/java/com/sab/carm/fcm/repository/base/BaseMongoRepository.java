package com.sab.carm.fcm.repository.base;

import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Reusable MongoTemplate-backed repository operations.
 */
@Repository
public class BaseMongoRepository {

    private final MongoTemplate mongoTemplate;

    public BaseMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public <T> T save(T entity) {
        return mongoTemplate.save(entity);
    }

    public <T> List<T> insertAll(Collection<T> entities, Class<T> entityType) {
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entityType);
        operations.insert(entities);
        operations.execute();
        return new java.util.ArrayList<>(entities);
    }

    public MongoTemplate template() {
        return mongoTemplate;
    }
}
