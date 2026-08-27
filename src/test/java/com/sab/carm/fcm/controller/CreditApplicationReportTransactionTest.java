package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.CarmFcmTransactionContext;
import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.service.CreditApplicationReportCsvWriter;
import com.sab.carm.fcm.service.CreditApplicationReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreditApplicationReportTransactionTest {

    @AfterEach
    void tearDown() {
        CarmFcmTransactionContext.clear();
    }

    @Test
    void reportShouldExposeTransactionIdInResponseHeader() {

        CreditApplicationReportService service =
                mock(CreditApplicationReportService.class);

        CreditApplicationReportCsvWriter writer =
                mock(CreditApplicationReportCsvWriter.class);

        CreditApplicationReportResponse report =
                mock(CreditApplicationReportResponse.class);

        when(service.getReport("REL001", "001"))
                .thenReturn(report);

        when(writer.write(report))
                .thenReturn("facilityNo,facilityType\\n123,1200\\n");

        CarmFcmTransactionContext.initialize(
                "CARM-001",
                "FCM-TXN-001");

        CreditApplicationReportController controller =
                new CreditApplicationReportController(
                        service,
                        writer);

        org.springframework.http.ResponseEntity<String> response =
                controller.getReport(
                        "CARM-001",
                        "REL001",
                        "001");

        assertEquals(
                "CARM-001",
                response.getHeaders().getFirst(
                        CreditApplicationReportController
                                .CORRELATION_ID_HEADER));

        assertEquals(
                "FCM-TXN-001",
                response.getHeaders().getFirst(
                        CreditApplicationReportController
                                .TRANSACTION_ID_HEADER));
    }
}
