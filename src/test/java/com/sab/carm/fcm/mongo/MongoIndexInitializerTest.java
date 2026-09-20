package com.sab.carm.fcm.mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoIndexInitializerTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoProperties mongoProperties;

    @Mock
    private MongoProperties.DbCollectionNames collectionNames;

    @Mock
    private MongoProperties.CollectionDefinition collectionDefinition;

    @Mock
    private MongoProperties.IndexDefinition indexDefinition;

    @Mock
    private IndexOperations indexOperations;

    @Mock
    private IndexInfo existingIndex;

    private MongoIndexInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MongoIndexInitializer(
                mongoTemplate,
                mongoProperties);
    }

    @Test
    void shouldSkipIndexInitializationWhenDisabled() {

        when(mongoProperties.isInitializeIndexes())
                .thenReturn(false);

        initializer.initialize();

        verifyNoInteractions(mongoTemplate);
    }

    @Test
    void shouldCreateConfiguredIndexWhenMissing() {

        Map<String, Integer> fields =
                new LinkedHashMap<>();
        fields.put("relationshipId", 1);
        fields.put("serialNo", 1);

        when(mongoProperties.isInitializeIndexes())
                .thenReturn(true);

        when(mongoProperties.getDbCollectionNames())
                .thenReturn(collectionNames);

        when(collectionNames.asList())
                .thenReturn(Collections.singletonList(
                        collectionDefinition));

        when(collectionDefinition.getName())
                .thenReturn("facilityCapitalMarkers");

        when(collectionDefinition.getIndexes())
                .thenReturn(Collections.singletonList(
                        indexDefinition));

        when(indexDefinition.getName())
                .thenReturn("facility_capital_markers_credit_application");

        when(indexDefinition.getFields())
                .thenReturn(fields);

        when(indexDefinition.isUnique())
                .thenReturn(false);

        when(mongoTemplate.collectionExists(
                "facilityCapitalMarkers"))
                .thenReturn(true);

        when(mongoTemplate.indexOps(
                "facilityCapitalMarkers"))
                .thenReturn(indexOperations);

        when(indexOperations.getIndexInfo())
                .thenReturn(Collections.emptyList());

        initializer.initialize();

        verify(indexOperations).ensureIndex(any());
    }

    @Test
    void shouldNotCreateIndexWhenIndexAlreadyExists() {

        when(mongoProperties.isInitializeIndexes())
                .thenReturn(true);

        when(mongoProperties.getDbCollectionNames())
                .thenReturn(collectionNames);

        when(collectionNames.asList())
                .thenReturn(Collections.singletonList(
                        collectionDefinition));

        when(collectionDefinition.getName())
                .thenReturn("facilityCapitalMarkers");

        when(collectionDefinition.getIndexes())
                .thenReturn(Collections.singletonList(
                        indexDefinition));

        when(indexDefinition.getName())
                .thenReturn("facility_capital_markers_business_key");

        when(mongoTemplate.collectionExists(
                "facilityCapitalMarkers"))
                .thenReturn(true);

        when(mongoTemplate.indexOps(
                "facilityCapitalMarkers"))
                .thenReturn(indexOperations);

        when(indexOperations.getIndexInfo())
                .thenReturn(Collections.singletonList(
                        existingIndex));

        when(existingIndex.getName())
                .thenReturn("facility_capital_markers_business_key");

        initializer.initialize();

        verify(indexOperations, never())
                .ensureIndex(any());
    }

    @Test
    void shouldSkipIndexesWhenCollectionDoesNotExist() {

        when(mongoProperties.isInitializeIndexes())
                .thenReturn(true);

        when(mongoProperties.getDbCollectionNames())
                .thenReturn(collectionNames);

        when(collectionNames.asList())
                .thenReturn(Collections.singletonList(
                        collectionDefinition));

        when(collectionDefinition.getName())
                .thenReturn("facilityCapitalMarkers");

        when(collectionDefinition.getIndexes())
                .thenReturn(Collections.singletonList(
                        indexDefinition));

        when(mongoTemplate.collectionExists(
                "facilityCapitalMarkers"))
                .thenReturn(false);

        initializer.initialize();

        verify(mongoTemplate, never())
                .indexOps(anyString());
    }
}
