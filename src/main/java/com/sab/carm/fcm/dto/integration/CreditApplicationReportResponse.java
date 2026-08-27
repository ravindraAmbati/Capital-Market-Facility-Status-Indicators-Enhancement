package com.sab.carm.fcm.dto.integration;

import com.sab.carm.fcm.entity.CreditApplicationConsent;

import java.util.ArrayList;
import java.util.List;

public class CreditApplicationReportResponse {

    private String relationshipId;
    private String serialNo;

    private List<FacilityCapitalMarkersReportRow> facilities =
            new ArrayList<>();

    private List<CreditApplicationConsent.Consent> consents =
            new ArrayList<>();

    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String value) {
        this.relationshipId = value;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public List<FacilityCapitalMarkersReportRow> getFacilities() {
        return facilities;
    }

    public void setFacilities(
            List<FacilityCapitalMarkersReportRow> value) {
        this.facilities = value;
    }

    public List<CreditApplicationConsent.Consent> getConsents() {
        return consents;
    }

    public void setConsents(
            List<CreditApplicationConsent.Consent> value) {
        this.consents = value;
    }
}
