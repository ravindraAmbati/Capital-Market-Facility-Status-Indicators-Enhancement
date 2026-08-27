package com.sab.carm.fcm.service;

import com.sab.carm.fcm.entity.ApiAudit;
import com.sab.carm.fcm.repository.ApiAuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ApiAuditServiceFailureTest {

    @Mock
    private ApiAuditRepository repository;

    @InjectMocks
    private ApiAuditService service;

    @Test
    void auditFailureMustNotPropagate() {

        doThrow(new RuntimeException("Mongo unavailable"))
                .when(repository)
                .save(any(ApiAudit.class));

        String transactionId = service.audit(
                "CARM-001",
                "GET",
                "/api/carm/fcm/facility",
                "FACILITY_GET",
                "FAILED",
                "REL-001",
                "01",
                "123",
                null,
                Collections.emptyMap());

        assertNotNull(transactionId);
    }
}
