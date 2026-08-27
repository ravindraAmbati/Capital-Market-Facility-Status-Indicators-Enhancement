package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.FacilityCapitalMarkersHistory;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FacilityCapitalMarkersHistoryRepositoryContractTest {

    @Test
    void shouldExtendMongoRepository() {
        assertTrue(MongoRepository.class.isAssignableFrom(
                FacilityCapitalMarkersHistoryRepository.class));
    }

    @Test
    void shouldUseHistoryEntityAndStringId() {
        assertTrue(FacilityCapitalMarkersHistoryRepository.class
                .getGenericInterfaces()[0]
                .getTypeName()
                .contains(FacilityCapitalMarkersHistory.class.getSimpleName()));
    }
}
