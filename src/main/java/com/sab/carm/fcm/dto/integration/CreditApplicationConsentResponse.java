package com.sab.carm.fcm.dto.integration;

import com.sab.carm.fcm.entity.CreditApplicationConsent;

public class CreditApplicationConsentResponse {

    private String relationshipId;
    private String serialNo;
    private CreditApplicationConsent.Consent consent;

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

    public CreditApplicationConsent.Consent getConsent() {
        return consent;
    }

    public void setConsent(CreditApplicationConsent.Consent value) {
        this.consent = value;
    }
}
