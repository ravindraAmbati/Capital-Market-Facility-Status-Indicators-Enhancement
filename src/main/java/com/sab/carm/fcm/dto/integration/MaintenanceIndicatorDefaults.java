package com.sab.carm.fcm.dto.integration;

public class MaintenanceIndicatorDefaults {
    private String advised;
    private String committed;
    private String unconditionalCancellable;

    public String getAdvised() { return advised; }
    public void setAdvised(String value) { this.advised = value; }

    public String getCommitted() { return committed; }
    public void setCommitted(String value) { this.committed = value; }

    public String getUnconditionalCancellable() { return unconditionalCancellable; }
    public void setUnconditionalCancellable(String value) { this.unconditionalCancellable = value; }
}
