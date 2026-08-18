package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurposeCodeMaintenanceRepository
        extends MongoRepository<PurposeCodeMaintenance, String> {

    Optional<PurposeCodeMaintenance>
    findByPurposeCodeHubAndPurposeCodeCarm(
            String purposeCodeHub,
            String purposeCodeCarm);
}