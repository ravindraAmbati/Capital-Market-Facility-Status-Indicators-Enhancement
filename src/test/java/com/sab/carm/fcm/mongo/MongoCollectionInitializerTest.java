package com.sab.carm.fcm.mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoCollectionInitializerTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoProperties mongoProperties;

    @Mock
    private MongoProperties.DbCollectionNames collectionNames;

    @Mock
    private MongoProperties.CollectionDefinition collectionDefinition;

    private MongoCollectionInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new MongoCollectionInitializer(
                mongoTemplate,
                mongoProperties);
    }

    @Test
    void shouldSkipCollectionInitializationWhenDisabled() {

        when(mongoProperties.isInitializeCollections())
                .thenReturn(false);

        initializer.initialize();

        verifyNoInteractions(mongoTemplate);
    }

    @Test
    void shouldCreateConfiguredCollectionWhenMissing() {

        when(mongoProperties.isInitializeCollections())
                .thenReturn(true);

        when(mongoProperties.getDbCollectionNames())
                .thenReturn(collectionNames);

        when(collectionNames.asList())
                .thenReturn(Collections.singletonList(
                        collectionDefinition));

        when(collectionDefinition.isCreateIfMissing())
                .thenReturn(true);

        when(collectionDefinition.getName())
                .thenReturn("facilityCapitalMarkers");

        when(mongoTemplate.collectionExists(
                "facilityCapitalMarkers"))
                .thenReturn(false);

        initializer.initialize();

        verify(mongoTemplate).createCollection(
                "facilityCapitalMarkers");
    }

    @Test
    void shouldNotCreateCollectionWhenAlreadyExists() {

        when(mongoProperties.isInitializeCollections())
                .thenReturn(true);

        when(mongoProperties.getDbCollectionNames())
                .thenReturn(collectionNames);

        when(collectionNames.asList())
                .thenReturn(Collections.singletonList(
                        collectionDefinition));

        when(collectionDefinition.isCreateIfMissing())
                .thenReturn(true);

        when(collectionDefinition.getName())
                .thenReturn("facilityCapitalMarkers");

        when(mongoTemplate.collectionExists(
                "facilityCapitalMarkers"))
                .thenReturn(true);

        initializer.initialize();

        verify(mongoTemplate, never())
                .createCollection(anyString());
    }

    @Test
    void shouldSkipCollectionWhenCreateIfMissingIsFalse() {

        when(mongoProperties.isInitializeCollections())
                .thenReturn(true);

        when(mongoProperties.getDbCollectionNames())
                .thenReturn(collectionNames);

        when(collectionNames.asList())
                .thenReturn(Collections.singletonList(
                        collectionDefinition));

        when(collectionDefinition.isCreateIfMissing())
                .thenReturn(false);

        initializer.initialize();

        verifyNoInteractions(mongoTemplate);
    }
}
