package com.sab.carm.fcm.dto.integration;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * CARM business payload for Facility Capital Markers create/update.
 * X-CARM-FCM-CorrelationId is an HTTP header and is not part of this DTO.
 */
public class FacilityCapitalMarkersRequest {

    @NotBlank
    private String creditApplicationRelationshipId;

    @NotBlank
    private String serialNo;

    @NotBlank
    private String facilityNo;

    private String customerId;
    private String borrowingGroup;
    private String proposalType;
    private String applicationStatus;

    @NotBlank
    private String facilityType;

    @NotBlank
    private String carmPurposeCode;

    @Valid
    @NotNull
    private CapitalMarkerRequest advised;

    @Valid
    @NotNull
    private CapitalMarkerRequest committed;

    @Valid
    @NotNull
    private CapitalMarkerRequest unconditionalCancellable;

    private String standingSecurityDocument;
    private String seniorityType;
    private String updatedBy;
    private String updatedDateTime;

    public String getCreditApplicationRelationshipId() { return creditApplicationRelationshipId; }
    public void setCreditApplicationRelationshipId(String value) { this.creditApplicationRelationshipId = value; }

    public String getSerialNo() { return serialNo; }
    public void setSerialNo(String value) { this.serialNo = value; }

    public String getFacilityNo() { return facilityNo; }
    public void setFacilityNo(String value) { this.facilityNo = value; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String value) { this.customerId = value; }

    public String getBorrowingGroup() { return borrowingGroup; }
    public void setBorrowingGroup(String value) { this.borrowingGroup = value; }

    public String getProposalType() { return proposalType; }
    public void setProposalType(String value) { this.proposalType = value; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String value) { this.applicationStatus = value; }

    public String getFacilityType() { return facilityType; }
    public void setFacilityType(String value) { this.facilityType = value; }

    public String getCarmPurposeCode() { return carmPurposeCode; }
    public void setCarmPurposeCode(String value) { this.carmPurposeCode = value; }

    public CapitalMarkerRequest getAdvised() { return advised; }
    public void setAdvised(CapitalMarkerRequest value) { this.advised = value; }

    public CapitalMarkerRequest getCommitted() { return committed; }
    public void setCommitted(CapitalMarkerRequest value) { this.committed = value; }

    public CapitalMarkerRequest getUnconditionalCancellable() { return unconditionalCancellable; }
    public void setUnconditionalCancellable(CapitalMarkerRequest value) { this.unconditionalCancellable = value; }

    public String getStandingSecurityDocument() { return standingSecurityDocument; }
    public void setStandingSecurityDocument(String value) { this.standingSecurityDocument = value; }

    public String getSeniorityType() { return seniorityType; }
    public void setSeniorityType(String value) { this.seniorityType = value; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String value) { this.updatedBy = value; }

    public String getUpdatedDateTime() { return updatedDateTime; }
    public void setUpdatedDateTime(String value) { this.updatedDateTime = value; }

    public static class CapitalMarkerRequest {
        @NotBlank
        @Pattern(regexp = "[YN]", message = "indicator must be Y or N")
        private String indicator;

        private boolean override;
        private String overrideJustification;

        public String getIndicator() { return indicator; }
        public void setIndicator(String value) { this.indicator = value; }

        public boolean isOverride() { return override; }
        public void setOverride(boolean value) { this.override = value; }

        public String getOverrideJustification() { return overrideJustification; }
        public void setOverrideJustification(String value) { this.overrideJustification = value; }
    }
}
