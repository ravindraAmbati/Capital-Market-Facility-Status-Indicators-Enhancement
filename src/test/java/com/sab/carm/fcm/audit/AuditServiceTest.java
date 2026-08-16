package com.sab.carm.fcm.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.repository.AuditRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditRecordRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    private AuditService auditService;

    @BeforeEach
    void setUp() {

        auditService =
                new AuditService(
                        repository,
                        objectMapper);
    }

    @Test
    void shouldWriteAuditEventToApplicationLogAndMongo() throws JsonProcessingException {

        AuditEvent event =
                AuditEvent.loginSuccess(
                        "sa-svc-carm-admin",
                        "ADMIN",
                        "10.10.10.10",
                        "corr-123");

        when(objectMapper.writeValueAsString(event))
                .thenReturn(
                        "{\"eventType\":\"LOGIN\"}");

        auditService.record(event);

        verify(objectMapper)
                .writeValueAsString(event);

        verify(repository)
                .save(any());
    }

    @Test
    void shouldContinueWhenMongoPersistenceFails() throws JsonProcessingException {

        AuditEvent event =
                AuditEvent.loginFailure(
                        "unknown-user",
                        "10.10.10.10",
                        "corr-123",
                        "USER_NOT_FOUND");

        when(objectMapper.writeValueAsString(event))
                .thenReturn(
                        "{\"eventType\":\"LOGIN\"}");

        when(repository.save(any()))
                .thenThrow(
                        new RuntimeException(
                                "Mongo unavailable"));

        auditService.record(event);

        verify(repository)
                .save(any());
    }

    @Test
    void shouldContinueWhenAuditJsonSerializationFails() throws JsonProcessingException {

        AuditEvent event =
                AuditEvent.loginSuccess(
                        "sa-svc-carm-admin",
                        "ADMIN",
                        "10.10.10.10",
                        "corr-123");

        when(objectMapper.writeValueAsString(event))
                .thenThrow(
                        new JsonProcessingException(
                                "serialization failed") {
                        });

        auditService.record(event);

        verify(repository)
                .save(any());
    }
}