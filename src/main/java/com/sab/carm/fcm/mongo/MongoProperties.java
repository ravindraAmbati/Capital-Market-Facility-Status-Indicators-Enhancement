package com.sab.carm.fcm.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        private CollectionDefinition facilityCapitalMarkers;
        private CollectionDefinition facilityCapitalMarkersHistory;
        private CollectionDefinition creditApplicationConsent;
        private CollectionDefinition apiAudit;
        private CollectionDefinition auditRecords;
        private CollectionDefinition applicationVersion;
        private CollectionDefinition referenceDataMappings;
        private CollectionDefinition facilityTypeMaintenance;
        private CollectionDefinition purposeCodeMaintenance;
        private CollectionDefinition maintenanceHistory;

        public CollectionDefinition getFacilityCapitalMarkers() {
            return facilityCapitalMarkers;
        }

        public void setFacilityCapitalMarkers(CollectionDefinition value) {
            this.facilityCapitalMarkers = value;
        }

        public CollectionDefinition getFacilityCapitalMarkersHistory() {
            return facilityCapitalMarkersHistory;
        }

        public void setFacilityCapitalMarkersHistory(CollectionDefinition value) {
            this.facilityCapitalMarkersHistory = value;
        }

        public CollectionDefinition getCreditApplicationConsent() {
            return creditApplicationConsent;
        }

        public void setCreditApplicationConsent(CollectionDefinition value) {
            this.creditApplicationConsent = value;
        }

        public CollectionDefinition getApiAudit() {
            return apiAudit;
        }

        public void setApiAudit(CollectionDefinition value) {
            this.apiAudit = value;
        }

        public CollectionDefinition getAuditRecords() {
            return auditRecords;
        }

        public void setAuditRecords(CollectionDefinition value) {
            this.auditRecords = value;
        }

        public CollectionDefinition getApplicationVersion() {
            return applicationVersion;
        }

        public void setApplicationVersion(CollectionDefinition value) {
            this.applicationVersion = value;
        }

        public CollectionDefinition getReferenceDataMappings() {
            return referenceDataMappings;
        }

        public void setReferenceDataMappings(CollectionDefinition value) {
            this.referenceDataMappings = value;
        }

        public CollectionDefinition getFacilityTypeMaintenance() {
            return facilityTypeMaintenance;
        }

        public void setFacilityTypeMaintenance(CollectionDefinition value) {
            this.facilityTypeMaintenance = value;
        }

        public CollectionDefinition getPurposeCodeMaintenance() {
            return purposeCodeMaintenance;
        }

        public void setPurposeCodeMaintenance(CollectionDefinition value) {
            this.purposeCodeMaintenance = value;
        }

        public CollectionDefinition getMaintenanceHistory() {
            return maintenanceHistory;
        }

        public void setMaintenanceHistory(CollectionDefinition value) {
            this.maintenanceHistory = value;
        }

        public List<CollectionDefinition> asList() {
            return Arrays.asList(
                    facilityCapitalMarkers,
                    facilityCapitalMarkersHistory,
                    creditApplicationConsent,
                    apiAudit,
                    auditRecords,
                    applicationVersion,
                    referenceDataMappings,
                    facilityTypeMaintenance,
                    purposeCodeMaintenance,
                    maintenanceHistory);
        }
    }

    public static class CollectionDefinition {

        private String name;
        private boolean createIfMissing;
        private List<IndexDefinition> indexes = Collections.emptyList();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCreateIfMissing() {
            return createIfMissing;
        }

        public void setCreateIfMissing(boolean createIfMissing) {
            this.createIfMissing = createIfMissing;
        }

        public List<IndexDefinition> getIndexes() {
            return indexes;
        }

        public void setIndexes(List<IndexDefinition> indexes) {
            this.indexes = indexes == null
                    ? Collections.<IndexDefinition>emptyList()
                    : indexes;
        }
    }

    public static class IndexDefinition {

        private String name;
        private Map<String, Integer> fields =
                new LinkedHashMap<>();
        private boolean unique;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Integer> getFields() {
            return fields;
        }

        public void setFields(Map<String, Integer> fields) {
            this.fields = fields == null
                    ? new LinkedHashMap<String, Integer>()
                    : fields;
        }

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }
    }
}
