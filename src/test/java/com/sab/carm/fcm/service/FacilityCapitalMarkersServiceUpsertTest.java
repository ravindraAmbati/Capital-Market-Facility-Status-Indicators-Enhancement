package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersRequest;
import com.sab.carm.fcm.dto.integration.FacilityOperation;
import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import com.sab.carm.fcm.entity.FacilityCapitalMarkersHistory;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersHistoryRepository;
import com.sab.carm.fcm.repository.FacilityCapitalMarkersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersServiceUpsertTest {

    @Mock
    private FacilityCapitalMarkersRepository repository;

    @Mock
    private FacilityCapitalMarkersHistoryRepository historyRepository;

    private FacilityCapitalMarkersService service;

    @BeforeEach
    void setUp() {
        service = new FacilityCapitalMarkersService(
                repository, historyRepository);
    }

    @Test
    void shouldCreateWhenCurrentRecordDoesNotExist() {
        FacilityCapitalMarkersRequest request = request("Y");

        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.empty());

        when(repository.save(any(FacilityCapitalMarkers.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FacilityOperation operation =
                service.upsert(request, "CARM-001").getOperation();

        assertEquals(FacilityOperation.CREATED, operation);
        verify(repository).save(any(FacilityCapitalMarkers.class));
        verifyNoInteractions(historyRepository);
    }

    @Test
    void shouldReturnNoChangeAndNotWriteWhenRequestIsIdentical() {
        FacilityCapitalMarkersRequest request = request("Y");

        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.of(entity("Y")));

        FacilityOperation operation =
                service.upsert(request, "CARM-001").getOperation();

        assertEquals(FacilityOperation.NO_CHANGE, operation);
        verify(repository, never()).save(any(FacilityCapitalMarkers.class));
        verifyNoInteractions(historyRepository);
    }

    @Test
    void shouldMoveCurrentToHistoryAndUpdateExistingCurrentWhenDifferent() {
        FacilityCapitalMarkers current = entity("Y");
        FacilityCapitalMarkersRequest request = request("N");

        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.of(current));

        when(historyRepository.save(any(FacilityCapitalMarkersHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(repository.save(any(FacilityCapitalMarkers.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersOperationResponse result =
                service.upsert(request, "CARM-002");

        assertEquals(FacilityOperation.UPDATED, result.getOperation());

        verify(historyRepository).save(argThat(history ->
                "123".equals(history.getFacilityNo())
                        && "123".equals(history.getOriginalFacilityNo())
                        && "UPDATE".equals(history.getAction())
                        && "CARM-002".equals(history.getCorrelationId())
                        && history.getTransactionId() != null));

        verify(repository).save(argThat(updated ->
                updated == current
                        && "N".equals(updated.getAdvised().getIndicator())
                        && "CARM-002".equals(updated.getCorrelationId())));
    }

    private FacilityCapitalMarkersRequest request(String indicator) {
        FacilityCapitalMarkersRequest request =
                new FacilityCapitalMarkersRequest();

        request.setCreditApplicationRelationshipId("REL001");
        request.setSerialNo("001");
        request.setFacilityNo("123");
        request.setFacilityType("FT01");
        request.setCarmPurposeCode("PURP01");
        request.setAdvised(marker(indicator));
        request.setCommitted(marker("Y"));
        request.setUnconditionalCancellable(marker("Y"));

        return request;
    }

    private FacilityCapitalMarkers entity(String indicator) {
        FacilityCapitalMarkers entity =
                new FacilityCapitalMarkers();

        entity.setCreditApplicationRelationshipId("REL001");
        entity.setSerialNo("001");
        entity.setFacilityNo("123");
        entity.setFacilityType("FT01");
        entity.setCarmPurposeCode("PURP01");
        entity.setAdvised(entityMarker(indicator));
        entity.setCommitted(entityMarker("Y"));
        entity.setUnconditionalCancellable(entityMarker("Y"));

        return entity;
    }

    private FacilityCapitalMarkersRequest.CapitalMarkerRequest marker(
            String indicator) {

        FacilityCapitalMarkersRequest.CapitalMarkerRequest marker =
                new FacilityCapitalMarkersRequest.CapitalMarkerRequest();

        marker.setIndicator(indicator);
        return marker;
    }

    private FacilityCapitalMarkers.CapitalMarker entityMarker(
            String indicator) {

        FacilityCapitalMarkers.CapitalMarker marker =
                new FacilityCapitalMarkers.CapitalMarker();

        marker.setIndicator(indicator);
        return marker;
    }
}
