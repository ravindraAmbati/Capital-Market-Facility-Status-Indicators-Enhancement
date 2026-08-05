package com.company.application.audit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.company.application.entity.AuditRecord;
import com.company.application.repository.AuditRecordRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuditServiceTest {

    @Test
    void writesAuditRecord() {
        AuditRecordRepository repository = Mockito.mock(AuditRecordRepository.class);
        AuditService service = new AuditService(repository);

        service.record("LOGIN", "admin1", "cid");

        verify(repository).save(any(AuditRecord.class));
    }
}
