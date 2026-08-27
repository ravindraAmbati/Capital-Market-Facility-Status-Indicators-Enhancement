package com.sab.carm.fcm.dto.integration;

public class PurposeCodeDefault {

    private String purposeCodeCarm;
    private String purposeCodeHub;
    private String purposeCodeDescription;
    private String unconditionalCancellable;

    public String getPurposeCodeCarm() {
        return purposeCodeCarm;
    }

    public void setPurposeCodeCarm(String value) {
        this.purposeCodeCarm = value;
    }

    public String getPurposeCodeHub() {
        return purposeCodeHub;
    }

    public void setPurposeCodeHub(String value) {
        this.purposeCodeHub = value;
    }

    public String getPurposeCodeDescription() {
        return purposeCodeDescription;
    }

    public void setPurposeCodeDescription(String value) {
        this.purposeCodeDescription = value;
    }

    public String getUnconditionalCancellable() {
        return unconditionalCancellable;
    }

    public void setUnconditionalCancellable(String value) {
        this.unconditionalCancellable = value;
    }
}
