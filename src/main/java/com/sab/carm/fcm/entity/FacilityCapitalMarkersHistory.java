package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Immutable historical snapshot of a FacilityCapitalMarkers record.
 *
 * This entity deliberately does not extend FacilityCapitalMarkers.
 * The history schema is independent from the current operational schema.
 */
@Document(collection = "facilityCapitalMarkersHistory")
public class FacilityCapitalMarkersHistory extends BaseEntity {

    @Field("creditApplicationRelationshipId")
    private String creditApplicationRelationshipId;

    @Field("serialNo")
    private String serialNo;

    /**
     * For a normal historical version this contains the original facility number.
     * For DELETE it is changed to <facilityNo>_DELETED_<correlationId>.
     */
    @Field("facilityNo")
    private String facilityNo;

    /**
     * Original business facility number, retained for production support and
     * facility-number reuse scenarios.
     */
    @Field("originalFacilityNo")
    private String originalFacilityNo;

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
    private FacilityCapitalMarkers.CapitalMarker advised;

    @Field("committed")
    private FacilityCapitalMarkers.CapitalMarker committed;

    @Field("unconditionalCancellable")
    private FacilityCapitalMarkers.CapitalMarker unconditionalCancellable;

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

    @Field("transactionId")
    private String transactionId;

    @Field("action")
    private String action;

    public String getCreditApplicationRelationshipId() {
        return creditApplicationRelationshipId;
    }

    public void setCreditApplicationRelationshipId(String value) {
        this.creditApplicationRelationshipId = value;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    public String getFacilityNo() {
        return facilityNo;
    }

    public void setFacilityNo(String value) {
        this.facilityNo = value;
    }

    public String getOriginalFacilityNo() {
        return originalFacilityNo;
    }

    public void setOriginalFacilityNo(String value) {
        this.originalFacilityNo = value;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String value) {
        this.customerId = value;
    }

    public String getBorrowingGroup() {
        return borrowingGroup;
    }

    public void setBorrowingGroup(String value) {
        this.borrowingGroup = value;
    }

    public String getProposalType() {
        return proposalType;
    }

    public void setProposalType(String value) {
        this.proposalType = value;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String value) {
        this.applicationStatus = value;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String value) {
        this.facilityType = value;
    }

    public String getCarmPurposeCode() {
        return carmPurposeCode;
    }

    public void setCarmPurposeCode(String value) {
        this.carmPurposeCode = value;
    }

    public FacilityCapitalMarkers.CapitalMarker getAdvised() {
        return advised;
    }

    public void setAdvised(FacilityCapitalMarkers.CapitalMarker value) {
        this.advised = value;
    }

    public FacilityCapitalMarkers.CapitalMarker getCommitted() {
        return committed;
    }

    public void setCommitted(FacilityCapitalMarkers.CapitalMarker value) {
        this.committed = value;
    }

    public FacilityCapitalMarkers.CapitalMarker getUnconditionalCancellable() {
        return unconditionalCancellable;
    }

    public void setUnconditionalCancellable(
            FacilityCapitalMarkers.CapitalMarker value) {
        this.unconditionalCancellable = value;
    }

    public String getStandingSecurityDocument() {
        return standingSecurityDocument;
    }

    public void setStandingSecurityDocument(String value) {
        this.standingSecurityDocument = value;
    }

    public String getSeniorityType() {
        return seniorityType;
    }

    public void setSeniorityType(String value) {
        this.seniorityType = value;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String value) {
        this.updatedBy = value;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String value) {
        this.updatedDateTime = value;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String value) {
        this.correlationId = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String value) {
        this.action = value;
    }
}
