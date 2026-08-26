package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersReportRow;
import com.sab.carm.fcm.service.CreditApplicationReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditApplicationReportControllerTest {

    @Mock
    private CreditApplicationReportService service;

    private CreditApplicationReportController controller;

    @BeforeEach
    void setUp() {
        controller = new CreditApplicationReportController(service);
    }

    @Test
    void shouldReturnCsv() {
        CreditApplicationReportResponse report =
                new CreditApplicationReportResponse();

        report.setRelationshipId("REL001");
        report.setSerialNo("001");

        FacilityCapitalMarkersReportRow row =
                new FacilityCapitalMarkersReportRow();
        row.setRelationshipId("REL001");
        row.setSerialNo("001");
        row.setFacilityNo("123");
        row.setCustomerId("CUST,001");
        row.setAdvised("Y");

        report.getFacilities().add(row);

        when(service.getReport("REL001", "001"))
                .thenReturn(report);

        ResponseEntity<String> response =
                controller.getReport(
                        "CARM-CORR-001",
                        "REL001",
                        "001");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().startsWith(
                "RELATIONSHIP_ID,SERIAL_NO,FACILITY_NO"));
        assertTrue(response.getBody().contains("\"CUST,001\""));
        assertEquals("text/csv",
                response.getHeaders().getContentType().toString());
    }
}
