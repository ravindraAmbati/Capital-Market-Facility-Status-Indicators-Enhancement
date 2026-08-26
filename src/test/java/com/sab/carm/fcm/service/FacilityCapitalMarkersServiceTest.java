package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersResponse;
import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersServiceTest {

    @Mock
    private FacilityCapitalMarkersRepository repository;

    private FacilityCapitalMarkersService service;

    @Test
    void shouldReturnFacilityWhenRecordExists() {
        service = new FacilityCapitalMarkersService(repository);

        FacilityCapitalMarkers entity = entity();

        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.of(entity));

        Optional<FacilityCapitalMarkersResponse> result =
                service.find("REL001", "001", "123");

        assertTrue(result.isPresent());
        assertEquals("REL001",
                result.get().getCreditApplicationRelationshipId());
        assertEquals("001", result.get().getSerialNo());
        assertEquals("123", result.get().getFacilityNo());
        assertEquals("FT01", result.get().getFacilityType());
        assertEquals("PURP01", result.get().getCarmPurposeCode());
        assertEquals("Y", result.get().getAdvised().getIndicator());
        assertTrue(result.get().getAdvised().isOverride());

        verify(repository)
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123");
    }

    @Test
    void shouldReturnEmptyWhenRecordDoesNotExist() {
        service = new FacilityCapitalMarkersService(repository);

        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.empty());

        Optional<FacilityCapitalMarkersResponse> result =
                service.find("REL001", "001", "123");

        assertTrue(!result.isPresent());
    }

    private FacilityCapitalMarkers entity() {
        FacilityCapitalMarkers entity = new FacilityCapitalMarkers();
        entity.setCreditApplicationRelationshipId("REL001");
        entity.setSerialNo("001");
        entity.setFacilityNo("123");
        entity.setFacilityType("FT01");
        entity.setCarmPurposeCode("PURP01");

        FacilityCapitalMarkers.CapitalMarker advised =
                new FacilityCapitalMarkers.CapitalMarker();
        advised.setIndicator("Y");
        advised.setOverride(true);
        advised.setOverrideJustification("Approved");
        entity.setAdvised(advised);

        return entity;
    }
}
