package com.sab.carm.fcm.carm.dto;

public class PurposeCodeMaintenanceData {

    private String purposeCodeHub;
    private String purposeCodeCarm;
    private String description;
    private String unconditionalCancellable;

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
}