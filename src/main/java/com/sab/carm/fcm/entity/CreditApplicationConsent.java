package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "creditApplicationConsent")
public class CreditApplicationConsent extends BaseEntity {

    @Field("relationshipId")
    private String relationshipId;

    @Field("serialNo")
    private String serialNo;

    @Field("consents")
    private List<Consent> consents = new ArrayList<>();

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

    public List<Consent> getConsents() {
        return consents;
    }

    public void setConsents(List<Consent> value) {
        this.consents = value;
    }

    public static class Consent {

        @Field("decision")
        private String decision;

        @Field("hubUserId")
        private String hubUserId;

        @Field("consentedAt")
        private Instant consentedAt;

        @Field("correlationId")
        private String correlationId;

        @Field("transactionId")
        private String transactionId;

        public String getDecision() {
            return decision;
        }

        public void setDecision(String value) {
            this.decision = value;
        }

        public String getHubUserId() {
            return hubUserId;
        }

        public void setHubUserId(String value) {
            this.hubUserId = value;
        }

        public Instant getConsentedAt() {
            return consentedAt;
        }

        public void setConsentedAt(Instant value) {
            this.consentedAt = value;
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
    }
}
