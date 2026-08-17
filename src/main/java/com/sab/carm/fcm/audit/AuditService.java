package com.sab.carm.fcm.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.entity.AuditRecord;
import com.sab.carm.fcm.repository.AuditRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Central audit service.
 *
 * The same AuditEvent is written to:
 *
 * 1. Application JSON log
 * 2. MongoDB, when MongoDB is available
 *
 * MongoDB failure must never prevent the application
 * authentication/request flow from continuing.
 */
@Service
public class AuditService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(AuditService.class);

    private final AuditRecordRepository repository;
    private final ObjectMapper objectMapper;

    public AuditService(
            AuditRecordRepository repository,
            ObjectMapper objectMapper) {

        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void record(AuditEvent event) {

        writeApplicationLog(event);

        writeMongo(event);
    }

    private void writeApplicationLog(
            AuditEvent event) {

        try {

            String json =
                    objectMapper.writeValueAsString(event);

            LOGGER.info("AUDIT_EVENT {}", json);

        } catch (JsonProcessingException ex) {

            /*
             * Audit logging must never break the business flow.
             */
            LOGGER.error(
                    "Unable to serialize audit event " +
                            "eventType={} username={}",
                    event.getEventType(),
                    event.getUsername(),
                    ex);
        }
    }

    private void writeMongo(
            AuditEvent event) {

        try {

            repository.save(
                    new AuditRecord(
                            event.getEventType(),
                            event.getResult(),
                            event.getUsername(),
                            event.getRole(),
                            event.getClientIp(),
                            event.getCorrelationId(),
                            event.getTimestamp(),
                            event.getReason(),
                            event.getDetails()));

        } catch (RuntimeException ex) {

            /*
             * MongoDB is currently not guaranteed to be
             * available. Never fail authentication or
             * request processing because audit persistence
             * failed.
             */
            LOGGER.warn(
                    "Mongo audit persistence failed " +
                            "eventType={} username={}",
                    event.getEventType(),
                    event.getUsername());
        }
    }
}