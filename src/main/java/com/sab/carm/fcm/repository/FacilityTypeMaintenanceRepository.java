package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityTypeMaintenanceRepository
        extends MongoRepository<FacilityTypeMaintenance, String> {

    Optional<FacilityTypeMaintenance> findByFacilityTypeCode(
            String facilityTypeCode);
}