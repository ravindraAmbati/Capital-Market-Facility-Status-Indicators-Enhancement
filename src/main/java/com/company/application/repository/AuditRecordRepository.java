package com.company.application.repository;

import com.company.application.entity.AuditRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for audit records.
 */
public interface AuditRecordRepository extends MongoRepository<AuditRecord, String> {
}
