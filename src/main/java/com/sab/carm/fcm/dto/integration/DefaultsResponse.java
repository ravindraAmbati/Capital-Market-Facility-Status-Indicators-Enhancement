package com.sab.carm.fcm.dto.integration;

import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import java.util.ArrayList;
import java.util.List;

public class DefaultsResponse {

    private List<FacilityTypeMaintenanceResponse> facilityTypes =
            new ArrayList<>();

    private List<PurposeCodeMaintenanceResponse> purposeCodes =
            new ArrayList<>();

    public List<FacilityTypeMaintenanceResponse> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(
            List<FacilityTypeMaintenanceResponse> facilityTypes) {
        this.facilityTypes = facilityTypes;
    }

    public List<PurposeCodeMaintenanceResponse> getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(
            List<PurposeCodeMaintenanceResponse> purposeCodes) {
        this.purposeCodes = purposeCodes;
    }
}
