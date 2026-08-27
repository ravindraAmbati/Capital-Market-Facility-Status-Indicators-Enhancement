package com.sab.carm.fcm.dto.integration;

public class MaintenanceData {
    private String type;
    private String code;
    private String description;
    private MaintenanceIndicatorDefaults indicators;

    public String getType() { return type; }
    public void setType(String value) { this.type = value; }

    public String getCode() { return code; }
    public void setCode(String value) { this.code = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public MaintenanceIndicatorDefaults getIndicators() { return indicators; }
    public void setIndicators(MaintenanceIndicatorDefaults value) { this.indicators = value; }
}
