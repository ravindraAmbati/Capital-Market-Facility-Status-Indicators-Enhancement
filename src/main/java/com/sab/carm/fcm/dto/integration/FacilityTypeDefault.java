package com.sab.carm.fcm.dto.integration;

public class FacilityTypeDefault {

    private String facilityTypeCode;
    private String facilityTypeDescription;
    private String advised;
    private String committed;

    public String getFacilityTypeCode() {
        return facilityTypeCode;
    }

    public void setFacilityTypeCode(String value) {
        this.facilityTypeCode = value;
    }

    public String getFacilityTypeDescription() {
        return facilityTypeDescription;
    }

    public void setFacilityTypeDescription(String value) {
        this.facilityTypeDescription = value;
    }

    public String getAdvised() {
        return advised;
    }

    public void setAdvised(String value) {
        this.advised = value;
    }

    public String getCommitted() {
        return committed;
    }

    public void setCommitted(String value) {
        this.committed = value;
    }
}
