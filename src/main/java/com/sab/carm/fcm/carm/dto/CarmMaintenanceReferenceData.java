package com.sab.carm.fcm.carm.dto;

import java.util.ArrayList;
import java.util.List;

public class CarmMaintenanceReferenceData {

    private List<FacilityTypeMaintenanceData> facilityTypes =
            new ArrayList<>();

    private List<PurposeCodeMaintenanceData> purposeCodes =
            new ArrayList<>();

    public List<FacilityTypeMaintenanceData> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(
            List<FacilityTypeMaintenanceData> facilityTypes) {

        this.facilityTypes = facilityTypes;
    }

    public List<PurposeCodeMaintenanceData> getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(
            List<PurposeCodeMaintenanceData> purposeCodes) {

        this.purposeCodes = purposeCodes;
    }
}