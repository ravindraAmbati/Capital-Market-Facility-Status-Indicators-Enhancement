package com.sab.carm.fcm.audit;

import com.sab.carm.fcm.entity.AuditRecord;
import com.sab.carm.fcm.repository.AuditRecordRepository;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Writes audit events through a single reusable entry point.
 */
@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private final AuditRecordRepository repository;

    public AuditService(AuditRecordRepository repository) {
        this.repository = repository;
    }

    public void record(String eventType, String username, String correlationId) {
        record(eventType, username, correlationId, Collections.emptyMap());
    }

    public void record(String eventType, String username, String correlationId, Map<String, String> details) {
        try {
            repository.save(new AuditRecord(eventType, username, correlationId, details));
        } catch (RuntimeException ex) {
            LOGGER.warn("Audit write failed for eventType={} username={}", eventType, username);
        }
    }
}
