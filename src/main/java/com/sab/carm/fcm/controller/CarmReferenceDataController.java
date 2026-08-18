package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.ReferenceDataRefreshResponse;
import com.sab.carm.fcm.service.CarmMaintenanceSyncService;
import com.sab.carm.fcm.service.CarmMaintenanceSyncService.SyncResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carm/reference-data")
public class CarmReferenceDataController {

    private final CarmMaintenanceSyncService syncService;

    public CarmReferenceDataController(
            CarmMaintenanceSyncService syncService) {

        this.syncService = syncService;
    }

    @PostMapping("/refresh/{tableName}")
    public ReferenceDataRefreshResponse refresh(
            @PathVariable String tableName) {

        SyncResult result =
                syncService.sync(tableName);

        ReferenceDataRefreshResponse response =
                new ReferenceDataRefreshResponse();

        response.setStatus("SUCCESS");
        response.setTableName(tableName);
        response.setSummary(result.getSummary());
        response.setDetails(result.getDetails());

        return response;
    }
}