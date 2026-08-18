package com.sab.carm.fcm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.dto.RefreshDetail;
import com.sab.carm.fcm.dto.RefreshSummary;
import com.sab.carm.fcm.dto.ReferenceDataRefreshResponse;
import com.sab.carm.fcm.service.CarmMaintenanceSyncService;
import com.sab.carm.fcm.service.CarmMaintenanceSyncService.SyncResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarmReferenceDataControllerTest {

    @Mock
    private CarmMaintenanceSyncService syncService;

    private CarmReferenceDataController controller;

    @BeforeEach
    void setUp() {

        controller =
                new CarmReferenceDataController(
                        syncService);
    }

    @Test
    void shouldRefreshRequestedTable() {

        RefreshSummary summary =
                new RefreshSummary();

        summary.incrementCreated();
        summary.incrementUpdated();

        RefreshDetail detail =
                new RefreshDetail();

        detail.setAction("CREATE");
        detail.setBusinessKey("FAC001");

        SyncResult result =
                new SyncResult(
                        "1200",
                        summary,
                        Collections.singletonList(detail));

        when(syncService.sync("1200"))
                .thenReturn(result);

        ReferenceDataRefreshResponse response =
                controller.refresh("1200");

        assertEquals(
                "SUCCESS",
                response.getStatus());

        assertEquals(
                "1200",
                response.getTableName());

        assertEquals(
                1,
                response.getSummary().getCreated());

        assertEquals(
                1,
                response.getSummary().getUpdated());

        assertEquals(
                1,
                response.getDetails().size());

        assertEquals(
                "CREATE",
                response.getDetails()
                        .get(0)
                        .getAction());

        verify(syncService)
                .sync("1200");
    }

    @Test
    void shouldRefreshPurposeCodeTable() {

        RefreshSummary summary =
                new RefreshSummary();

        summary.incrementCreated();

        SyncResult result =
                new SyncResult(
                        "1060",
                        summary,
                        Collections.emptyList());

        when(syncService.sync("1060"))
                .thenReturn(result);

        ReferenceDataRefreshResponse response =
                controller.refresh("1060");

        assertEquals(
                "SUCCESS",
                response.getStatus());

        assertEquals(
                "1060",
                response.getTableName());

        assertEquals(
                1,
                response.getSummary().getCreated());

        verify(syncService)
                .sync("1060");
    }
}