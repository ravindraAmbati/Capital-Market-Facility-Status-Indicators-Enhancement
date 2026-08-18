package com.sab.carm.fcm.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class FacilityTypeIndicatorRequest {

    @NotNull
    @Pattern(regexp = "[YN]")
    private String advised;

    @NotNull
    @Pattern(regexp = "[YN]")
    private String committed;

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