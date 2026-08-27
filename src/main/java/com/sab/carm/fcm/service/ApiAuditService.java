package com.sab.carm.fcm.service;

import com.sab.carm.fcm.config.CarmFcmTransactionContext;
import com.sab.carm.fcm.entity.ApiAudit;
import com.sab.carm.fcm.repository.ApiAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ApiAuditService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ApiAuditService.class);

    private final ApiAuditRepository repository;

    public ApiAuditService(ApiAuditRepository repository) {
        this.repository = repository;
    }

    /**
     * Part 11 API. The transaction id is supplied by the
     * request transaction context and is never generated here.
     */
    public String audit(
            String correlationId,
            String transactionId,
            String httpMethod,
            String apiPath,
            String operation,
            String status,
            String relationshipId,
            String serialNo,
            String facilityNo,
            String userId,
            Map<String, Object> details) {

        if (transactionId == null
                || transactionId.trim().isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        persist(
                correlationId,
                transactionId,
                httpMethod,
                apiPath,
                operation,
                status,
                relationshipId,
                serialNo,
                facilityNo,
                userId,
                details);

        return transactionId;
    }

    /**
     * Backward-compatible overload for existing unit tests and
     * non-HTTP callers. For an HTTP request Part 11 context is
     * preferred.
     */
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

        String transactionId =
                CarmFcmTransactionContext.getTransactionId();

        if (transactionId == null
                || transactionId.trim().isEmpty()) {
            transactionId = UUID.randomUUID().toString();
        }

        return audit(
                correlationId,
                transactionId,
                httpMethod,
                apiPath,
                operation,
                status,
                relationshipId,
                serialNo,
                facilityNo,
                userId,
                details);
    }

    private void persist(
            String correlationId,
            String transactionId,
            String httpMethod,
            String apiPath,
            String operation,
            String status,
            String relationshipId,
            String serialNo,
            String facilityNo,
            String userId,
            Map<String, Object> details) {

        try {
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
                audit.setDetails(
                        new LinkedHashMap<>(details));
            }

            repository.save(audit);

        } catch (RuntimeException exception) {
            LOGGER.error(
                    "Unable to persist API audit. "
                            + "correlationId={}, transactionId={}, "
                            + "apiPath={}, operation={}",
                    correlationId,
                    transactionId,
                    apiPath,
                    operation,
                    exception);
        }
    }
}
