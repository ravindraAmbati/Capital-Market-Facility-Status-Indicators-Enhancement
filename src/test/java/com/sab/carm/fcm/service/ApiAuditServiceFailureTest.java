package com.sab.carm.fcm.service;

import com.sab.carm.fcm.repository.ApiAuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiAuditServiceFailureTest {

    @Mock
    private ApiAuditRepository repository;

    @Test
    void auditFailureMustNotFailBusinessCaller() {

        when(repository.save(any()))
                .thenThrow(new RuntimeException(
                        "Mongo unavailable"));

        ApiAuditService service =
                new ApiAuditService(repository);

        String transactionId = service.audit(
                "CARM-001",
                "POST",
                "/api/carm/fcm/facility",
                "FACILITY_UPSERT",
                "SUCCESS",
                "REL001",
                "001",
                "123",
                null,
                Collections.emptyMap());

        assertNotNull(transactionId);

        verify(repository).save(any());
    }
}
