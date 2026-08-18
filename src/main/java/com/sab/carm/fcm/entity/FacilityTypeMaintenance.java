package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "facilityTypeMaintenance")
public class FacilityTypeMaintenance extends BaseEntity {

    @Indexed(unique = true)
    @Field("facilityTypeCode")
    private String facilityTypeCode;

    @Field("facilityTypeDescription")
    private String facilityTypeDescription;

    @Field("advised")
    private String advised;

    @Field("committed")
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

    public void setFacilityTypeDescription(String facilityTypeDescription) {
        this.facilityTypeDescription = facilityTypeDescription;
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