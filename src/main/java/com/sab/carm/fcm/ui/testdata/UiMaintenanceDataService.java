package com.sab.carm.fcm.ui.testdata;

import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import com.sab.carm.fcm.service.MaintenanceService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UiMaintenanceDataService {

    private final MaintenanceService maintenanceService;
    private final UiTestDataStore testDataStore;
    private final UiTestDataProperties properties;

    public UiMaintenanceDataService(
            MaintenanceService maintenanceService,
            UiTestDataStore testDataStore,
            UiTestDataProperties properties) {

        this.maintenanceService = maintenanceService;
        this.testDataStore = testDataStore;
        this.properties = properties;
    }

    public void initialiseTestDataIfEnabled()
            throws IOException {

        if (!properties.isEnabled()) {
            return;
        }

        testDataStore.reset(
                properties.getDirectory()
                        + "test-"
                        + properties.getScenario()
                        + ".json");
    }

    public List<FacilityTypeMaintenanceResponse>
    getFacilityTypes() {

        return properties.isEnabled()
                ? testDataStore.getFacilityTypes()
                : maintenanceService.getFacilityTypes();
    }

    public List<PurposeCodeMaintenanceResponse>
    getPurposeCodes() {

        return properties.isEnabled()
                ? testDataStore.getPurposeCodes()
                : maintenanceService.getPurposeCodes();
    }

    public boolean facilityTypeExists(String code) {

        return properties.isEnabled()
                ? testDataStore.facilityTypeExists(code)
                : false;
    }

    public boolean purposeCodeExists(
            String hub,
            String carm) {

        return properties.isEnabled()
                ? testDataStore.purposeCodeExists(hub, carm)
                : false;
    }

    public void saveFacilityType(
            String code,
            String description,
            String advised,
            String committed,
            boolean isNew) {

        if (properties.isEnabled()) {
            testDataStore.saveFacilityType(
                    code,
                    description,
                    advised,
                    committed);
            return;
        }

        // Production path remains the existing controller/service flow.
        throw new IllegalStateException(
                "Production maintenance writes must use existing service path");
    }

    public void deleteFacilityType(String code) {

        if (properties.isEnabled()) {
            testDataStore.deleteFacilityType(code);
            return;
        }

        throw new IllegalStateException(
                "Production maintenance writes must use existing service path");
    }

    public void savePurposeCode(
            String hub,
            String carm,
            String description,
            String unconditionalCancellable,
            boolean isNew) {

        if (properties.isEnabled()) {
            testDataStore.savePurposeCode(
                    hub,
                    carm,
                    description,
                    unconditionalCancellable);
            return;
        }

        throw new IllegalStateException(
                "Production maintenance writes must use existing service path");
    }

    public void deletePurposeCode(
            String hub,
            String carm) {

        if (properties.isEnabled()) {
            testDataStore.deletePurposeCode(hub, carm);
            return;
        }

        throw new IllegalStateException(
                "Production maintenance writes must use existing service path");
    }

    public boolean isEnabled() {
        return properties.isEnabled();
    }
}
