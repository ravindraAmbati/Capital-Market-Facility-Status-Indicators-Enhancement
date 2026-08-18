package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "purposeCodeMaintenance")
@CompoundIndex(
        name = "purpose_code_business_key",
        def = "{'purposeCodeHub': 1, 'purposeCodeCarm': 1}",
        unique = true)
public class PurposeCodeMaintenance extends BaseEntity {

    @Field("purposeCodeHub")
    private String purposeCodeHub;

    @Field("purposeCodeCarm")
    private String purposeCodeCarm;

    @Field("description")
    private String description;

    @Field("unconditionalCancellable")
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

    public void setUnconditionalCancellable(String unconditionalCancellable) {
        this.unconditionalCancellable = unconditionalCancellable;
    }
}