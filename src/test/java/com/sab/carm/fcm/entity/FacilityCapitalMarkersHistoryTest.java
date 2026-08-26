package com.sab.carm.fcm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FacilityCapitalMarkersHistoryTest {

    @Test
    void shouldStoreOriginalFacilityNumberAndDeleteFacilityNumber() {
        FacilityCapitalMarkersHistory history =
                new FacilityCapitalMarkersHistory();

        history.setFacilityNo("123_DELETED_CARM-ABC123");
        history.setOriginalFacilityNo("123");
        history.setAction("DELETE");
        history.setCorrelationId("CARM-ABC123");
        history.setTransactionId("FCM-001");

        assertEquals("123_DELETED_CARM-ABC123",
                history.getFacilityNo());
        assertEquals("123", history.getOriginalFacilityNo());
        assertEquals("DELETE", history.getAction());
        assertEquals("CARM-ABC123",
                history.getCorrelationId());
        assertEquals("FCM-001",
                history.getTransactionId());
    }

    @Test
    void shouldStoreCompleteFacilitySnapshot() {
        FacilityCapitalMarkersHistory history =
                new FacilityCapitalMarkersHistory();

        history.setCreditApplicationRelationshipId("REL001");
        history.setSerialNo("001");
        history.setFacilityNo("123");
        history.setOriginalFacilityNo("123");
        history.setCustomerId("CUST001");
        history.setBorrowingGroup("BG01");
        history.setProposalType("PT01");
        history.setApplicationStatus("ACTIVE");
        history.setFacilityType("FT01");
        history.setCarmPurposeCode("PURP01");
        history.setStandingSecurityDocument("Y");
        history.setSeniorityType("SENIOR");
        history.setUpdatedBy("AB12");
        history.setUpdatedDateTime("20260826120000");

        assertEquals("REL001",
                history.getCreditApplicationRelationshipId());
        assertEquals("001", history.getSerialNo());
        assertEquals("123", history.getFacilityNo());
        assertEquals("123", history.getOriginalFacilityNo());
        assertEquals("CUST001", history.getCustomerId());
        assertEquals("BG01", history.getBorrowingGroup());
        assertEquals("PT01", history.getProposalType());
        assertEquals("ACTIVE", history.getApplicationStatus());
        assertEquals("FT01", history.getFacilityType());
        assertEquals("PURP01", history.getCarmPurposeCode());
        assertEquals("Y", history.getStandingSecurityDocument());
        assertEquals("SENIOR", history.getSeniorityType());
        assertEquals("AB12", history.getUpdatedBy());
        assertEquals("20260826120000",
                history.getUpdatedDateTime());
    }

    @Test
    void shouldAllowMultipleHistoryRecordsForSameFacilityNumber() {
        FacilityCapitalMarkersHistory first =
                new FacilityCapitalMarkersHistory();
        first.setFacilityNo("123_DELETED_CARM-001");
        first.setOriginalFacilityNo("123");

        FacilityCapitalMarkersHistory second =
                new FacilityCapitalMarkersHistory();
        second.setFacilityNo("123_DELETED_CARM-002");
        second.setOriginalFacilityNo("123");

        assertEquals("123", first.getOriginalFacilityNo());
        assertEquals("123", second.getOriginalFacilityNo());
        assertEquals(
                "123_DELETED_CARM-001",
                first.getFacilityNo());
        assertEquals(
                "123_DELETED_CARM-002",
                second.getFacilityNo());
    }
}
