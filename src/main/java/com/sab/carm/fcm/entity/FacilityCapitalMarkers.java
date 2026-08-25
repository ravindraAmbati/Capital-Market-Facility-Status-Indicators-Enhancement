package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "facilityCapitalMarkers")
@CompoundIndex(
        name = "facility_capital_markers_business_key",
        def = "{"
                + "'creditApplicationRelationshipId': 1,"
                + "'serialNo': 1,"
                + "'facilityNo': 1"
                + "}",
        unique = true)
public class FacilityCapitalMarkers extends BaseEntity {

    @Field("creditApplicationRelationshipId")
    private String creditApplicationRelationshipId;

    @Field("serialNo")
    private String serialNo;

    @Field("facilityNo")
    private String facilityNo;

    @Field("customerId")
    private String customerId;

    @Field("borrowingGroup")
    private String borrowingGroup;

    @Field("proposalType")
    private String proposalType;

    @Field("applicationStatus")
    private String applicationStatus;

    @Field("facilityType")
    private String facilityType;

    @Field("carmPurposeCode")
    private String carmPurposeCode;

    @Field("advised")
    private CapitalMarker advised;

    @Field("committed")
    private CapitalMarker committed;

    @Field("unconditionalCancellable")
    private CapitalMarker unconditionalCancellable;

    @Field("standingSecurityDocument")
    private String standingSecurityDocument;

    @Field("seniorityType")
    private String seniorityType;

    @Field("updatedBy")
    private String updatedBy;

    @Field("updatedDateTime")
    private String updatedDateTime;

    @Field("correlationId")
    private String correlationId;

    public String getCreditApplicationRelationshipId() {
        return creditApplicationRelationshipId;
    }

    public void setCreditApplicationRelationshipId(
            String creditApplicationRelationshipId) {

        this.creditApplicationRelationshipId =
                creditApplicationRelationshipId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getFacilityNo() {
        return facilityNo;
    }

    public void setFacilityNo(String facilityNo) {
        this.facilityNo = facilityNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBorrowingGroup() {
        return borrowingGroup;
    }

    public void setBorrowingGroup(String borrowingGroup) {
        this.borrowingGroup = borrowingGroup;
    }

    public String getProposalType() {
        return proposalType;
    }

    public void setProposalType(String proposalType) {
        this.proposalType = proposalType;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getCarmPurposeCode() {
        return carmPurposeCode;
    }

    public void setCarmPurposeCode(String carmPurposeCode) {
        this.carmPurposeCode = carmPurposeCode;
    }

    public CapitalMarker getAdvised() {
        return advised;
    }

    public void setAdvised(CapitalMarker advised) {
        this.advised = advised;
    }

    public CapitalMarker getCommitted() {
        return committed;
    }

    public void setCommitted(CapitalMarker committed) {
        this.committed = committed;
    }

    public CapitalMarker getUnconditionalCancellable() {
        return unconditionalCancellable;
    }

    public void setUnconditionalCancellable(
            CapitalMarker unconditionalCancellable) {

        this.unconditionalCancellable =
                unconditionalCancellable;
    }

    public String getStandingSecurityDocument() {
        return standingSecurityDocument;
    }

    public void setStandingSecurityDocument(
            String standingSecurityDocument) {

        this.standingSecurityDocument =
                standingSecurityDocument;
    }

    public String getSeniorityType() {
        return seniorityType;
    }

    public void setSeniorityType(String seniorityType) {
        this.seniorityType = seniorityType;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public static class CapitalMarker {

        @Field("indicator")
        private String indicator;

        @Field("override")
        private boolean override;

        @Field("overrideJustification")
        private String overrideJustification;

        public String getIndicator() {
            return indicator;
        }

        public void setIndicator(String indicator) {
            this.indicator = indicator;
        }

        public boolean isOverride() {
            return override;
        }

        public void setOverride(boolean override) {
            this.override = override;
        }

        public String getOverrideJustification() {
            return overrideJustification;
        }

        public void setOverrideJustification(
                String overrideJustification) {

            this.overrideJustification =
                    overrideJustification;
        }
    }
}