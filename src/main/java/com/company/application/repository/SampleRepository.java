package com.company.application.repository;

import com.company.application.entity.SampleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for sample records.
 */
public interface SampleRepository extends MongoRepository<SampleEntity, String> {
}
