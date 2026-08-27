package com.sab.carm.fcm.dto.integration;

public class FacilityCapitalMarkersOperationResponse {
    private FacilityOperation operation;
    private FacilityCapitalMarkersResponse facilityCapitalMarkers;

    public FacilityOperation getOperation() { return operation; }
    public void setOperation(FacilityOperation value) { this.operation = value; }

    public FacilityCapitalMarkersResponse getFacilityCapitalMarkers() {
        return facilityCapitalMarkers;
    }

    public void setFacilityCapitalMarkers(FacilityCapitalMarkersResponse value) {
        this.facilityCapitalMarkers = value;
    }
}
