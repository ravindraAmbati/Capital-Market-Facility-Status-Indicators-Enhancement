package com.sab.carm.fcm.service;

import com.sab.carm.fcm.entity.ApiAudit;
import com.sab.carm.fcm.repository.ApiAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiAuditServiceTest {

    @Mock
    private ApiAuditRepository repository;

    private ApiAuditService service;

    @BeforeEach
    void setUp() {
        service = new ApiAuditService(repository);
    }

    @Test
    void shouldPersistApiAuditWithTraceData() {
        String transactionId = service.audit(
                "CARM-CORR-001",
                "POST",
                "/api/carm/fcm/facility",
                "POST",
                "SUCCESS",
                "REL001",
                "001",
                "123",
                "AB12",
                Collections.emptyMap());

        assertNotNull(transactionId);

        ArgumentCaptor<ApiAudit> captor =
                ArgumentCaptor.forClass(ApiAudit.class);

        verify(repository).save(captor.capture());

        ApiAudit audit = captor.getValue();

        assertEquals("CARM-CORR-001", audit.getCorrelationId());
        assertEquals(transactionId, audit.getTransactionId());
        assertEquals("POST", audit.getHttpMethod());
        assertEquals(
                "/api/carm/fcm/facility",
                audit.getApiPath());
        assertEquals("SUCCESS", audit.getStatus());
        assertEquals("REL001", audit.getRelationshipId());
        assertEquals("001", audit.getSerialNo());
        assertEquals("123", audit.getFacilityNo());
        assertEquals("AB12", audit.getUserId());
        assertNotNull(audit.getTimestamp());
    }
}
