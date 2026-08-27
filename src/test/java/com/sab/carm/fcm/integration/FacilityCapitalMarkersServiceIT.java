package com.sab.carm.fcm.integration;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersOperationResponse;
import com.sab.carm.fcm.dto.integration.FacilityOperation;
import com.sab.carm.fcm.entity.FacilityCapitalMarkersHistory;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersHistoryRepository;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import com.sab.carm.fcm.service.FacilityCapitalMarkersService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(FacilityCapitalMarkersServiceIT.Config.class)
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=${MONGODB_IT_URI}"
})
class FacilityCapitalMarkersServiceIT {

    private static final String REL = "IT-REL-001";
    private static final String SERIAL = "001";
    private static final String FACILITY = "123";

    @Autowired private FacilityCapitalMarkersService service;
    @Autowired private FacilityCapitalMarkersRepository repository;
    @Autowired private FacilityCapitalMarkersHistoryRepository historyRepository;

    @BeforeEach
    @AfterEach
    void cleanup() {
        repository.deleteAll();
        historyRepository.deleteAll();
    }

    @Test
    void createShouldPersistCurrentFacility() {
        FacilityCapitalMarkersOperationResponse r =
            service.upsert(request("Y","Y","N"), "IT-CREATE");

        assertEquals(FacilityOperation.CREATED, r.getOperation());
        assertTrue(repository
            .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                REL, SERIAL, FACILITY).isPresent());
        assertEquals(0, historyRepository.count());
    }

    @Test
    void identicalPostShouldReturnNoChange() {
        FacilityCapitalMarkersRequest req = request("Y","Y","N");
        service.upsert(req, "IT-FIRST");

        FacilityCapitalMarkersOperationResponse r =
            service.upsert(req, "IT-RETRY");

        assertEquals(FacilityOperation.NO_CHANGE, r.getOperation());
        assertEquals(1, repository.count());
        assertEquals(0, historyRepository.count());
    }

    @Test
    void changedPostShouldCreateHistoryAndUpdateCurrent() {
        service.upsert(request("Y","Y","N"), "IT-FIRST");

        FacilityCapitalMarkersOperationResponse r =
            service.upsert(request("N","Y","Y"), "IT-UPDATE");

        assertEquals(FacilityOperation.UPDATED, r.getOperation());
        assertEquals(1, repository.count());
        assertEquals(1, historyRepository.count());

        FacilityCapitalMarkersHistory h = historyRepository.findAll().get(0);
        assertEquals(FACILITY, h.getFacilityNo());
        assertEquals(FACILITY, h.getOriginalFacilityNo());
        assertEquals("IT-UPDATE", h.getCorrelationId());
        assertEquals("UPDATE", h.getAction());
        assertEquals("Y", h.getAdvised().getIndicator());

        assertEquals("N", repository
            .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                REL, SERIAL, FACILITY).get().getAdvised().getIndicator());
    }

    @Test
    void deleteShouldPhysicallyRemoveCurrentAndPreserveHistory() {
        service.upsert(request("Y","Y","N"), "IT-CREATE");

        assertTrue(service.delete(REL, SERIAL, FACILITY, "IT-DELETE"));

        assertFalse(repository
            .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                REL, SERIAL, FACILITY).isPresent());

        assertEquals(1, historyRepository.count());
        FacilityCapitalMarkersHistory h = historyRepository.findAll().get(0);

        assertEquals("123_DELETED_IT-DELETE", h.getFacilityNo());
        assertEquals("123", h.getOriginalFacilityNo());
        assertEquals("DELETE", h.getAction());
        assertEquals("IT-DELETE", h.getCorrelationId());
    }

    @Test
    void deletedFacilityNumberCanBeReused() {
        service.upsert(request("Y","Y","N"), "IT-FIRST-CREATE");
        service.delete(REL, SERIAL, FACILITY, "IT-FIRST-DELETE");

        FacilityCapitalMarkersOperationResponse r =
            service.upsert(request("N","N","Y"), "IT-SECOND-CREATE");

        assertEquals(FacilityOperation.CREATED, r.getOperation());
        assertTrue(repository
            .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                REL, SERIAL, FACILITY).isPresent());
        assertEquals(1, historyRepository.count());
        assertEquals("123_DELETED_IT-FIRST-DELETE",
            historyRepository.findAll().get(0).getFacilityNo());
    }

    private FacilityCapitalMarkersRequest request(String a, String c, String u) {
        FacilityCapitalMarkersRequest r = new FacilityCapitalMarkersRequest();
        r.setCreditApplicationRelationshipId(REL);
        r.setSerialNo(SERIAL);
        r.setFacilityNo(FACILITY);
        r.setCustomerId("IT-CUSTOMER");
        r.setBorrowingGroup("IT-GROUP");
        r.setProposalType("NEW");
        r.setApplicationStatus("ACTIVE");
        r.setFacilityType("FT-IT");
        r.setCarmPurposeCode("PC-IT");
        r.setAdvised(marker(a));
        r.setCommitted(marker(c));
        r.setUnconditionalCancellable(marker(u));
        r.setStandingSecurityDocument("SSD-IT");
        r.setSeniorityType("SENIOR");
        r.setUpdatedBy("IT-USER");
        r.setUpdatedDateTime("2026-08-27T10:00:00Z");
        return r;
    }

    private FacilityCapitalMarkersRequest.CapitalMarkerRequest marker(String value) {
        FacilityCapitalMarkersRequest.CapitalMarkerRequest m =
            new FacilityCapitalMarkersRequest.CapitalMarkerRequest();
        m.setIndicator(value);
        m.setOverride(false);
        return m;
    }

    @TestConfiguration
    static class Config {
        @Bean
        FacilityCapitalMarkersService facilityCapitalMarkersService(
                FacilityCapitalMarkersRepository repository,
                FacilityCapitalMarkersHistoryRepository historyRepository) {
            return new FacilityCapitalMarkersService(repository, historyRepository);
        }
    }
}
