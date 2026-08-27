package com.sab.carm.fcm.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FacilityCapitalMarkersTest {

    @Test
    void shouldCreateFacilityCapitalMarkers() {

        FacilityCapitalMarkers markers =
                new FacilityCapitalMarkers();

        markers.setCreditApplicationRelationshipId(
                "123456789");

        markers.setSerialNo("001");

        markers.setFacilityNo("0001");

        markers.setCustomerId("CUST001");

        markers.setBorrowingGroup("BG001");

        markers.setProposalType("NEW");

        markers.setApplicationStatus("ACTIVE");

        markers.setFacilityType("FAC001");

        markers.setCarmPurposeCode("PUR001");

        FacilityCapitalMarkers.CapitalMarker advised =
                new FacilityCapitalMarkers.CapitalMarker();

        advised.setIndicator("Y");
        advised.setOverride(false);

        FacilityCapitalMarkers.CapitalMarker committed =
                new FacilityCapitalMarkers.CapitalMarker();

        committed.setIndicator("N");
        committed.setOverride(true);
        committed.setOverrideJustification(
                "Approved exception");

        FacilityCapitalMarkers.CapitalMarker
                unconditionalCancellable =
                new FacilityCapitalMarkers.CapitalMarker();

        unconditionalCancellable.setIndicator("Y");
        unconditionalCancellable.setOverride(false);

        markers.setAdvised(advised);
        markers.setCommitted(committed);
        markers.setUnconditionalCancellable(
                unconditionalCancellable);

        markers.setStandingSecurityDocument("Y");
        markers.setSeniorityType("SENIOR");

        markers.setUpdatedBy("AB12");
        markers.setUpdatedDateTime(
                "2026-08-18T10:30:00");

        markers.setCorrelationId(
                "CARM-ABC-123");

        assertEquals(
                "123456789",
                markers.getCreditApplicationRelationshipId());

        assertEquals(
                "001",
                markers.getSerialNo());

        assertEquals(
                "0001",
                markers.getFacilityNo());

        assertEquals(
                "CUST001",
                markers.getCustomerId());

        assertEquals(
                "BG001",
                markers.getBorrowingGroup());

        assertEquals(
                "NEW",
                markers.getProposalType());

        assertEquals(
                "ACTIVE",
                markers.getApplicationStatus());

        assertEquals(
                "FAC001",
                markers.getFacilityType());

        assertEquals(
                "PUR001",
                markers.getCarmPurposeCode());

        assertEquals(
                "Y",
                markers.getAdvised().getIndicator());

        assertFalse(
                markers.getAdvised().isOverride());

        assertEquals(
                "N",
                markers.getCommitted().getIndicator());

        assertTrue(
                markers.getCommitted().isOverride());

        assertEquals(
                "Approved exception",
                markers.getCommitted()
                        .getOverrideJustification());

        assertEquals(
                "Y",
                markers.getUnconditionalCancellable()
                        .getIndicator());

        assertEquals(
                "Y",
                markers.getStandingSecurityDocument());

        assertEquals(
                "SENIOR",
                markers.getSeniorityType());

        assertEquals(
                "AB12",
                markers.getUpdatedBy());

        assertEquals(
                "2026-08-18T10:30:00",
                markers.getUpdatedDateTime());

        assertEquals(
                "CARM-ABC-123",
                markers.getCorrelationId());
    }

    @Test
    void shouldStoreOverrideJustificationOnlyForOverriddenMarker() {

        FacilityCapitalMarkers.CapitalMarker marker =
                new FacilityCapitalMarkers.CapitalMarker();

        marker.setIndicator("N");
        marker.setOverride(true);
        marker.setOverrideJustification(
                "Business approved exception");

        assertEquals(
                "N",
                marker.getIndicator());

        assertTrue(marker.isOverride());

        assertEquals(
                "Business approved exception",
                marker.getOverrideJustification());
    }
}