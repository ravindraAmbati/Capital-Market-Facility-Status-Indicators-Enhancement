package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.FacilityCapitalMarkersHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityCapitalMarkersHistoryRepository
        extends MongoRepository<FacilityCapitalMarkersHistory, String> {
}
