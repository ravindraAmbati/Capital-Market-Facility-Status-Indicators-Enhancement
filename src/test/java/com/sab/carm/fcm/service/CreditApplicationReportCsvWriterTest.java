package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersReportRow;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditApplicationReportCsvWriterTest {

    private final CreditApplicationReportCsvWriter writer =
            new CreditApplicationReportCsvWriter();

    @Test
    void shouldWriteFacilitiesAndConsentsAsSeparateRows() {

        CreditApplicationReportResponse report =
                new CreditApplicationReportResponse();

        report.setRelationshipId("REL001");
        report.setSerialNo("001");

        FacilityCapitalMarkersReportRow facility1 =
                facility("123", "Customer, One");

        FacilityCapitalMarkersReportRow facility2 =
                facility("456", "Customer Two");

        report.getFacilities().add(facility1);
        report.getFacilities().add(facility2);

        CreditApplicationConsent.Consent recommend =
                consent("RECOMMEND", "AB12");

        CreditApplicationConsent.Consent approve =
                consent("APPROVE", "CD34");

        report.getConsents().add(recommend);
        report.getConsents().add(approve);

        String csv = writer.write(report);

        String[] lines = csv.split("\\r?\\n");

        // Header + 2 facility rows + 2 consent rows.
        assertEquals(5, lines.length);

        assertTrue(lines[1].startsWith(
                "\"FACILITY\",\"REL001\",\"001\",\"123\""));

        assertTrue(lines[2].startsWith(
                "\"FACILITY\",\"REL001\",\"001\",\"456\""));

        assertTrue(lines[3].startsWith(
                "\"CONSENT\",\"REL001\",\"001\""));

        assertTrue(lines[3].contains("\"RECOMMEND\""));
        assertTrue(lines[4].contains("\"APPROVE\""));

        // No facility x consent multiplication.
        assertEquals(1,
                count(lines, "\"FACILITY\",\"REL001\",\"001\",\"123\""));
        assertEquals(1,
                count(lines, "\"FACILITY\",\"REL001\",\"001\",\"456\""));
    }

    @Test
    void shouldEscapeCsvSpecialCharacters() {

        CreditApplicationReportResponse report =
                new CreditApplicationReportResponse();

        report.setRelationshipId("REL001");
        report.setSerialNo("001");

        report.getFacilities().add(
                facility("123", "Customer, \"Special\""));

        String csv = writer.write(report);

        assertTrue(csv.contains(
                "\"Customer, \"\"Special\"\"\""));
    }

    @Test
    void shouldWriteConsentWithoutFacilities() {

        CreditApplicationReportResponse report =
                new CreditApplicationReportResponse();

        report.setRelationshipId("REL001");
        report.setSerialNo("001");

        report.getConsents().add(
                consent("DECLINE", "EF56"));

        String csv = writer.write(report);

        String[] lines = csv.split("\\r?\\n");

        assertEquals(2, lines.length);
        assertTrue(lines[1].startsWith(
                "\"CONSENT\",\"REL001\",\"001\""));
        assertTrue(lines[1].contains("\"DECLINE\""));
        assertTrue(lines[1].contains("\"EF56\""));
    }

    @Test
    void shouldWriteHeaderWhenNoDataExists() {

        CreditApplicationReportResponse report =
                new CreditApplicationReportResponse();

        String csv = writer.write(report);

        assertEquals(1, csv.split("\\r?\\n").length);
    }

    private FacilityCapitalMarkersReportRow facility(
            String facilityNo,
            String customerId) {

        FacilityCapitalMarkersReportRow row =
                new FacilityCapitalMarkersReportRow();

        row.setRelationshipId("REL001");
        row.setSerialNo("001");
        row.setFacilityNo(facilityNo);
        row.setCustomerId(customerId);
        row.setFacilityType("FT01");
        row.setCarmPurposeCode("PUR01");
        row.setAdvised("Y");
        row.setCommitted("Y");
        row.setUnconditionalCancellable("N");

        return row;
    }

    private CreditApplicationConsent.Consent consent(
            String decision,
            String userId) {

        CreditApplicationConsent.Consent consent =
                new CreditApplicationConsent.Consent();

        consent.setDecision(decision);
        consent.setHubUserId(userId);
        consent.setCorrelationId("CARM-CORR");
        consent.setTransactionId("TXN");

        return consent;
    }

    private long count(String[] lines, String prefix) {
        return java.util.Arrays.stream(lines)
                .filter(line -> line.startsWith(prefix))
                .count();
    }
}
