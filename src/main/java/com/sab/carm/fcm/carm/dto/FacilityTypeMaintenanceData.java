package com.sab.carm.fcm.carm.dto;

public class FacilityTypeMaintenanceData {

    private String facilityTypeCode;
    private String facilityTypeDescription;
    private String advised;
    private String committed;

    public String getFacilityTypeCode() {
        return facilityTypeCode;
    }

    public void setFacilityTypeCode(String facilityTypeCode) {
        this.facilityTypeCode = facilityTypeCode;
    }

    public String getFacilityTypeDescription() {
        return facilityTypeDescription;
    }

    public void setFacilityTypeDescription(
            String facilityTypeDescription) {

        this.facilityTypeDescription =
                facilityTypeDescription;
    }

    public String getAdvised() {
        return advised;
    }

    public void setAdvised(String advised) {
        this.advised = advised;
    }

    public String getCommitted() {
        return committed;
    }

    public void setCommitted(String committed) {
        this.committed = committed;
    }
}