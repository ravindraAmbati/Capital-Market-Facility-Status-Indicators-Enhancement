package com.sab.carm.fcm.service;

import com.sab.carm.fcm.entity.ApiAudit;
import com.sab.carm.fcm.repository.ApiAuditRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiAuditService {

    private final ApiAuditRepository repository;

    public ApiAuditService(ApiAuditRepository repository) {
        this.repository = repository;
    }

    public String audit(
            String correlationId,
            String httpMethod,
            String apiPath,
            String operation,
            String status,
            String relationshipId,
            String serialNo,
            String facilityNo,
            String userId,
            Map<String, Object> details) {

        String transactionId = UUID.randomUUID().toString();

        ApiAudit audit = new ApiAudit();

        audit.setCorrelationId(correlationId);
        audit.setTransactionId(transactionId);
        audit.setHttpMethod(httpMethod);
        audit.setApiPath(apiPath);
        audit.setOperation(operation);
        audit.setStatus(status);
        audit.setRelationshipId(relationshipId);
        audit.setSerialNo(serialNo);
        audit.setFacilityNo(facilityNo);
        audit.setUserId(userId);
        audit.setTimestamp(Instant.now());

        if (details != null) {
            audit.setDetails(details);
        }

        repository.save(audit);

        return transactionId;
    }
}
