package com.company.application.mongo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.company.application.exception.MongoInfrastructureException;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

class MongoConnectionValidatorTest {

    @Test
    void wrapsConnectionFailures() {
        MongoTemplate template = mock(MongoTemplate.class);
        MongoDatabase database = mock(MongoDatabase.class);
        when(template.getDb()).thenReturn(database);
        when(database.runCommand(new Document("ping", 1))).thenThrow(new RuntimeException("down"));

        assertThatThrownBy(() -> new MongoConnectionValidator().validate(template, new MongoProperties()))
                .isInstanceOf(MongoInfrastructureException.class);
    }
}
