package com.sab.carm.fcm.carm;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "carm")
public class CarmProperties {

    private Api api = new Api();
    private String siteId;
    private ReferenceData referenceData = new ReferenceData();

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public ReferenceData getReferenceData() {
        return referenceData;
    }

    public void setReferenceData(ReferenceData referenceData) {
        this.referenceData = referenceData;
    }

    public static class Api {

        private String baseUrl;
        private String referenceDataPath;
        private String method;
        private String contentType;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getReferenceDataPath() {
            return referenceDataPath;
        }

        public void setReferenceDataPath(String referenceDataPath) {
            this.referenceDataPath = referenceDataPath;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

    public static class ReferenceData {

        private boolean loadOnStartup;
        private List<String> tables = new ArrayList<>();
        private Defaults defaults = new Defaults();

        public boolean isLoadOnStartup() {
            return loadOnStartup;
        }

        public void setLoadOnStartup(boolean loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }

        public List<String> getTables() {
            return tables;
        }

        public void setTables(List<String> tables) {
            this.tables = tables;
        }

        public Defaults getDefaults() {
            return defaults;
        }

        public void setDefaults(Defaults defaults) {
            this.defaults = defaults;
        }
    }

    public static class Defaults {

        private FacilityType facilityType = new FacilityType();
        private PurposeCode purposeCode = new PurposeCode();

        public FacilityType getFacilityType() {
            return facilityType;
        }

        public void setFacilityType(FacilityType facilityType) {
            this.facilityType = facilityType;
        }

        public PurposeCode getPurposeCode() {
            return purposeCode;
        }

        public void setPurposeCode(PurposeCode purposeCode) {
            this.purposeCode = purposeCode;
        }
    }

    public static class FacilityType {

        private String advised = "Y";
        private String committed = "Y";

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

    public static class PurposeCode {

        private String unconditionalCancellable = "Y";

        public String getUnconditionalCancellable() {
            return unconditionalCancellable;
        }

        public void setUnconditionalCancellable(
                String unconditionalCancellable) {

            this.unconditionalCancellable =
                    unconditionalCancellable;
        }
    }
}