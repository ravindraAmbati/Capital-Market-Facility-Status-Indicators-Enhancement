package com.sab.carm.fcm.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Sample entity used to validate repository wiring.
 */
@Document(collection = "samples")
public class SampleEntity extends BaseEntity {

    private String message;

    public SampleEntity() {
    }

    public SampleEntity(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
