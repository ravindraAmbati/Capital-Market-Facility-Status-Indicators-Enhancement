package com.sab.carm.fcm.mongo;

import java.util.List;
import org.bson.Document;

/**
 * Parsed reference data file contents.
 */
public class ReferenceDataFile {

    private final String collection;
    private final String keyField;
    private final List<Document> records;

    public ReferenceDataFile(String collection, String keyField, List<Document> records) {
        this.collection = collection;
        this.keyField = keyField;
        this.records = records;
    }

    public String getCollection() { return collection; }
    public String getKeyField() { return keyField; }
    public List<Document> getRecords() { return records; }
}
