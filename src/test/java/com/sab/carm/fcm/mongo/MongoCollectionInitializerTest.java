package com.sab.carm.fcm.mongo;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;

class MongoCollectionInitializerTest {

    @Test
    void createsMissingCollections() {
        MongoTemplate template = Mockito.mock(MongoTemplate.class);
        MongoProperties properties = new MongoProperties();
        properties.setDbCollectionNames(Collections.singletonMap("sample", "sampleCollection"));
        when(template.collectionExists("sampleCollection")).thenReturn(false);
        MongoStartupStatus status = new MongoStartupStatus();

        new MongoCollectionInitializer().initialize(template, properties, status);

        verify(template).createCollection("sampleCollection");
        org.assertj.core.api.Assertions.assertThat(status.getInitializedCollections()).contains("sampleCollection");
    }
}
