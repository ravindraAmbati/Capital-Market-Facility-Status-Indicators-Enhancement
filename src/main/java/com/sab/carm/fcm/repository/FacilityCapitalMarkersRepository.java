package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.FacilityCapitalMarkers;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityCapitalMarkersRepository
        extends MongoRepository<FacilityCapitalMarkers, String> {

    Optional<FacilityCapitalMarkers>
    findByRelationshipIdAndSerialNoAndFacilityNo(
            String relationshipId,
            String serialNo,
            String facilityNo);

    List<FacilityCapitalMarkers>
    findByRelationshipIdAndSerialNo(
            String relationshipId,
            String serialNo);
}
