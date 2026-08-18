package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.MaintenanceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaintenanceHistoryRepository
        extends MongoRepository<MaintenanceHistory, String> {
}