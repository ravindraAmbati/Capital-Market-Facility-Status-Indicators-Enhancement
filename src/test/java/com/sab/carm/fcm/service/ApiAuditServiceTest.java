package com.sab.carm.fcm.service;

import com.sab.carm.fcm.repository.ApiAuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiAuditServiceTest {

    @Mock
    private ApiAuditRepository repository;

    @Test
    void shouldPersistSuppliedTransactionId() {
        ApiAuditService service =
                new ApiAuditService(repository);

        service.audit(
                "CARM-001",
                "FCM-TXN-001",
                "POST",
                "/api/carm/fcm/facility",
                "FACILITY_UPSERT",
                "SUCCESS",
                "REL001",
                "001",
                "123",
                null,
                null);

        ArgumentCaptor<com.sab.carm.fcm.entity.ApiAudit> captor =
                ArgumentCaptor.forClass(
                        com.sab.carm.fcm.entity.ApiAudit.class);

        verify(repository).save(captor.capture());

        assertEquals(
                "CARM-001",
                captor.getValue().getCorrelationId());
        assertEquals(
                "FCM-TXN-001",
                captor.getValue().getTransactionId());
    }

    @Test
    void auditFailureMustNotFailBusinessCaller() {
        when(repository.save(any()))
                .thenThrow(new RuntimeException("Mongo unavailable"));

        ApiAuditService service =
                new ApiAuditService(repository);

        service.audit(
                "CARM-001",
                "FCM-TXN-001",
                "POST",
                "/api/carm/fcm/facility",
                "FACILITY_UPSERT",
                "SUCCESS",
                "REL001",
                "001",
                "123",
                null,
                null);

        verify(repository).save(any());
    }
}
