package com.company.application.mongo;

import com.company.application.exception.MongoInfrastructureException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.springframework.stereotype.Component;

/**
 * Parses and validates reference-data JSON files.
 */
@Component
public class ReferenceDataJsonParser {

    private final ObjectMapper objectMapper;

    public ReferenceDataJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ReferenceDataFile parse(InputStream inputStream) {
        try {
            JsonNode root = objectMapper.readTree(inputStream);
            String collection = requiredText(root, "collection");
            String keyField = requiredText(root, "keyField");
            JsonNode recordsNode = root.get("records");
            if (recordsNode == null || !recordsNode.isArray()) {
                throw new MongoInfrastructureException("Reference data records must be an array");
            }
            List<Document> records = new ArrayList<>();
            for (JsonNode record : recordsNode) {
                records.add(Document.parse(objectMapper.writeValueAsString(record)));
            }
            return new ReferenceDataFile(collection, keyField, records);
        } catch (IOException ex) {
            throw new MongoInfrastructureException("Invalid reference-data JSON", ex);
        }
    }

    private String requiredText(JsonNode root, String field) {
        JsonNode value = root.get(field);
        if (value == null || !value.isTextual()) {
            throw new MongoInfrastructureException("Reference data missing field: " + field);
        }
        return value.asText();
    }
}
