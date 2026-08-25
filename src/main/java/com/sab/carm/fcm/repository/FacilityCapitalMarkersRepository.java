package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityCapitalMarkersRepository
        extends MongoRepository<FacilityCapitalMarkers, String> {

    Optional<FacilityCapitalMarkers>
    findByCreditApplicationRelationshipIdAndSerialNoAndFacilityNo(
            String creditApplicationRelationshipId,
            String serialNo,
            String facilityNo);
}