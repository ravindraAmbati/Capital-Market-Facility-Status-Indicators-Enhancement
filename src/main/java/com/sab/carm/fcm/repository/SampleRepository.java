package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.SampleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for sample records.
 */
public interface SampleRepository extends MongoRepository<SampleEntity, String> {
}
