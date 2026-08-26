package com.sab.carm.fcm.service;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityCapitalMarkersServiceDeleteTest {

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
    void shouldMoveCurrentToHistoryAndDeleteCurrent() {
        FacilityCapitalMarkers current = current();

        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.of(current));

        when(historyRepository.save(any(FacilityCapitalMarkersHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = service.delete(
                "REL001", "001", "123", "CARM-DELETE-001");

        assertTrue(result);

        verify(historyRepository).save(argThat(history ->
                "123_DELETED_CARM-DELETE-001"
                        .equals(history.getFacilityNo())
                        && "123".equals(history.getOriginalFacilityNo())
                        && "REL001".equals(
                                history.getCreditApplicationRelationshipId())
                        && "001".equals(history.getSerialNo())
                        && "DELETE".equals(history.getAction())
                        && "CARM-DELETE-001".equals(
                                history.getCorrelationId())
                        && history.getTransactionId() != null));

        verify(repository).delete(current);
    }

    @Test
    void shouldNotDeleteAnythingWhenCurrentDoesNotExist() {
        when(repository
                .findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
                        "REL001", "001", "123"))
                .thenReturn(Optional.empty());

        boolean result = service.delete(
                "REL001", "001", "123", "CARM-DELETE-001");

        assertFalse(result);
        verifyNoInteractions(historyRepository);
        verify(repository, never()).delete(any(FacilityCapitalMarkers.class));
    }

    private FacilityCapitalMarkers current() {
        FacilityCapitalMarkers entity =
                new FacilityCapitalMarkers();

        entity.setCreditApplicationRelationshipId("REL001");
        entity.setSerialNo("001");
        entity.setFacilityNo("123");
        entity.setCustomerId("CUST001");
        entity.setFacilityType("FT01");
        entity.setCarmPurposeCode("PURP01");
        entity.setUpdatedBy("AB12");
        entity.setUpdatedDateTime("20260826120000");

        FacilityCapitalMarkers.CapitalMarker marker =
                new FacilityCapitalMarkers.CapitalMarker();
        marker.setIndicator("Y");
        marker.setOverride(true);
        marker.setOverrideJustification("Business justification");

        entity.setAdvised(marker);
        entity.setCommitted(marker);
        entity.setUnconditionalCancellable(marker);

        return entity;
    }
}
