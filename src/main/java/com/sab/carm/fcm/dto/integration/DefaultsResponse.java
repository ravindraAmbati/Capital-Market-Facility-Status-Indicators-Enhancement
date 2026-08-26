package com.sab.carm.fcm.dto.integration;

import java.util.ArrayList;
import java.util.List;

public class DefaultsResponse {
    private List<MaintenanceData> maintenanceData = new ArrayList<>();

    public List<MaintenanceData> getMaintenanceData() { return maintenanceData; }
    public void setMaintenanceData(List<MaintenanceData> value) { this.maintenanceData = value; }
}
