package com.sab.carm.fcm.dto.integration;

import java.util.List;

public class FacilityCapitalMarkersDefaultsResponse {

    private List<FacilityTypeDefault> facilityTypes;
    private List<PurposeCodeDefault> purposeCodes;

    public List<FacilityTypeDefault> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(List<FacilityTypeDefault> value) {
        this.facilityTypes = value;
    }

    public List<PurposeCodeDefault> getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(List<PurposeCodeDefault> value) {
        this.purposeCodes = value;
    }
}
