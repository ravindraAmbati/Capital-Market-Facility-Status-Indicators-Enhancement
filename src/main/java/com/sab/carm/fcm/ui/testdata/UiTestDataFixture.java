package com.sab.carm.fcm.ui.testdata;

import java.util.ArrayList;
import java.util.List;

public class UiTestDataFixture {

    private List<FacilityTypeFixture> facilityTypes =
            new ArrayList<FacilityTypeFixture>();

    private List<PurposeCodeFixture> purposeCodes =
            new ArrayList<PurposeCodeFixture>();

    public List<FacilityTypeFixture> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(
            List<FacilityTypeFixture> facilityTypes) {
        this.facilityTypes = facilityTypes;
    }

    public List<PurposeCodeFixture> getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(
            List<PurposeCodeFixture> purposeCodes) {
        this.purposeCodes = purposeCodes;
    }

    public static class FacilityTypeFixture {

        private String facilityTypeCode;
        private String facilityTypeDescription;
        private String advised;
        private String committed;
        private boolean active = true;

        public String getFacilityTypeCode() {
            return facilityTypeCode;
        }

        public void setFacilityTypeCode(String facilityTypeCode) {
            this.facilityTypeCode = facilityTypeCode;
        }

        public String getFacilityTypeDescription() {
            return facilityTypeDescription;
        }

        public void setFacilityTypeDescription(
                String facilityTypeDescription) {
            this.facilityTypeDescription =
                    facilityTypeDescription;
        }

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

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class PurposeCodeFixture {

        private String purposeCodeHub;
        private String purposeCodeCarm;
        private String description;
        private String unconditionalCancellable;
        private boolean active = true;

        public String getPurposeCodeHub() {
            return purposeCodeHub;
        }

        public void setPurposeCodeHub(String purposeCodeHub) {
            this.purposeCodeHub = purposeCodeHub;
        }

        public String getPurposeCodeCarm() {
            return purposeCodeCarm;
        }

        public void setPurposeCodeCarm(String purposeCodeCarm) {
            this.purposeCodeCarm = purposeCodeCarm;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUnconditionalCancellable() {
            return unconditionalCancellable;
        }

        public void setUnconditionalCancellable(
                String unconditionalCancellable) {
            this.unconditionalCancellable =
                    unconditionalCancellable;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
