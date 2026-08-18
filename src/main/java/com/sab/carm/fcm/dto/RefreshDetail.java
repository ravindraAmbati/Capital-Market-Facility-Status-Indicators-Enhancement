package com.sab.carm.fcm.dto;

public class RefreshDetail {

    private String action;
    private String businessKey;

    private Object previousData;
    private Object currentData;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Object getPreviousData() {
        return previousData;
    }

    public void setPreviousData(Object previousData) {
        this.previousData = previousData;
    }

    public Object getCurrentData() {
        return currentData;
    }

    public void setCurrentData(Object currentData) {
        this.currentData = currentData;
    }
}