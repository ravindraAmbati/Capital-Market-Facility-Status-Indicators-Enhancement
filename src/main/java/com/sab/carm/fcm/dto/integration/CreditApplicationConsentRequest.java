package com.sab.carm.fcm.dto.integration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreditApplicationConsentRequest {

    @NotBlank
    private String relationshipId;

    @NotBlank
    private String serialNo;

    @NotNull
    private DecisionType decision;

    @NotBlank
    private String hubUserId;

    public String getRelationshipId() { return relationshipId; }
    public void setRelationshipId(String value) { this.relationshipId = value; }

    public String getSerialNo() { return serialNo; }
    public void setSerialNo(String value) { this.serialNo = value; }

    public DecisionType getDecision() { return decision; }
    public void setDecision(DecisionType value) { this.decision = value; }

    public String getHubUserId() { return hubUserId; }
    public void setHubUserId(String value) { this.hubUserId = value; }
}
