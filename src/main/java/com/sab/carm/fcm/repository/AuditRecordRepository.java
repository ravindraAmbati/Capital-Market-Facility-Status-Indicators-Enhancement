package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.AuditRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for audit records.
 */
public interface AuditRecordRepository extends MongoRepository<AuditRecord, String> {
}
