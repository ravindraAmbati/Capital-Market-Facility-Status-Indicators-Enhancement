package com.company.application.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Sample entity used to validate repository wiring.
 */
@Document(collection = "samples")
public class SampleEntity {

    @Id
    private String id;
    private String message;

    public SampleEntity() {
    }

    public SampleEntity(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
