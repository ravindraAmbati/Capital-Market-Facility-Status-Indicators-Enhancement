package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.ApiAudit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiAuditRepository extends MongoRepository<ApiAudit, String> {
}
