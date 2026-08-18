package com.sab.carm.fcm.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PurposeCodeIndicatorRequest {

    @NotNull
    @Pattern(regexp = "[YN]")
    private String unconditionalCancellable;

    public String getUnconditionalCancellable() {
        return unconditionalCancellable;
    }

    public void setUnconditionalCancellable(
            String unconditionalCancellable) {

        this.unconditionalCancellable =
                unconditionalCancellable;
    }
}