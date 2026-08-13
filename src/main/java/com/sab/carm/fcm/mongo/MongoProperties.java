package com.sab.carm.fcm.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {

    private boolean validateConnection;

    private boolean initializeCollections;

    private boolean initializeIndexes;

    private DbCollectionNames dbCollectionNames = new DbCollectionNames();

    public boolean isValidateConnection() {
        return validateConnection;
    }

    public void setValidateConnection(boolean validateConnection) {
        this.validateConnection = validateConnection;
    }

    public boolean isInitializeCollections() {
        return initializeCollections;
    }

    public void setInitializeCollections(boolean initializeCollections) {
        this.initializeCollections = initializeCollections;
    }

    public boolean isInitializeIndexes() {
        return initializeIndexes;
    }

    public void setInitializeIndexes(boolean initializeIndexes) {
        this.initializeIndexes = initializeIndexes;
    }

    public DbCollectionNames getDbCollectionNames() {
        return dbCollectionNames;
    }

    public void setDbCollectionNames(DbCollectionNames dbCollectionNames) {
        this.dbCollectionNames = dbCollectionNames;
    }

    public static class DbCollectionNames {

        private String facilityCapitalMarkers;

        private String facilityCapitalMarkersDecisionHistory;

        private String creditApplicationCapitalMarkersReport;

        private String applicationAuditLog;

        private String referenceDataMappings;

        public String getFacilityCapitalMarkers() {
            return facilityCapitalMarkers;
        }

        public void setFacilityCapitalMarkers(String facilityCapitalMarkers) {
            this.facilityCapitalMarkers = facilityCapitalMarkers;
        }

        public String getFacilityCapitalMarkersDecisionHistory() {
            return facilityCapitalMarkersDecisionHistory;
        }

        public void setFacilityCapitalMarkersDecisionHistory(String facilityCapitalMarkersDecisionHistory) {
            this.facilityCapitalMarkersDecisionHistory = facilityCapitalMarkersDecisionHistory;
        }

        public String getCreditApplicationCapitalMarkersReport() {
            return creditApplicationCapitalMarkersReport;
        }

        public void setCreditApplicationCapitalMarkersReport(String creditApplicationCapitalMarkersReport) {
            this.creditApplicationCapitalMarkersReport = creditApplicationCapitalMarkersReport;
        }

        public String getApplicationAuditLog() {
            return applicationAuditLog;
        }

        public void setApplicationAuditLog(String applicationAuditLog) {
            this.applicationAuditLog = applicationAuditLog;
        }

        public String getReferenceDataMappings() {
            return referenceDataMappings;
        }

        public void setReferenceDataMappings(String referenceDataMappings) {
            this.referenceDataMappings = referenceDataMappings;
        }
    }
}