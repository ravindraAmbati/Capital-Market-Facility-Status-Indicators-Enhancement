package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersReportRow;
import com.sab.carm.fcm.service.CreditApplicationReportCsvWriter;
import com.sab.carm.fcm.service.CreditApplicationReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditApplicationReportControllerTest {

    @Mock
    private CreditApplicationReportService service;

    @Mock
    private CreditApplicationReportCsvWriter csvWriter;

    private CreditApplicationReportController controller;

    @BeforeEach
    void setUp() {
        controller = new CreditApplicationReportController(
                service,
                csvWriter);
    }

    @Test
    void shouldReturnCsv() {

        CreditApplicationReportResponse report =
                new CreditApplicationReportResponse();

        report.setRelationshipId("REL001");
        report.setSerialNo("001");

        FacilityCapitalMarkersReportRow row =
                new FacilityCapitalMarkersReportRow();

        row.setFacilityNo("123");
        report.getFacilities().add(row);

        when(service.getReport("REL001", "001"))
                .thenReturn(report);

        when(csvWriter.write(report))
                .thenReturn("RECORD_TYPE,RELATIONSHIP_ID\n"
                        + "FACILITY,REL001\n");

        ResponseEntity<String> response =
                controller.getReport(
                        "CARM-CORR-001",
                        "REL001",
                        "001");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(
                "text/csv",
                response.getHeaders()
                        .getContentType()
                        .toString());
        assertEquals(
                "RECORD_TYPE,RELATIONSHIP_ID\n"
                        + "FACILITY,REL001\n",
                response.getBody());

        assertEquals(
                "attachment; filename=\"REL001_001_facility-capital-markers.csv\"",
                response.getHeaders()
                        .getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }
}
