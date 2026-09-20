package com.sab.carm.fcm.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoConnectionValidatorTest {

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoDatabase adminDatabase;

    @Mock
    private MongoProperties mongoProperties;

    private MongoConnectionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MongoConnectionValidator(
                mongoClient,
                mongoProperties);
    }

    @Test
    void shouldSkipValidationWhenDisabled() {

        when(mongoProperties.isValidateConnection())
                .thenReturn(false);

        assertDoesNotThrow(() -> validator.validate());

        verifyNoInteractions(mongoClient);
    }

    @Test
    void shouldValidateConnectionWhenEnabled() {

        when(mongoProperties.isValidateConnection())
                .thenReturn(true);

        when(mongoClient.getDatabase("admin"))
                .thenReturn(adminDatabase);

        when(adminDatabase.runCommand(any(Document.class)))
                .thenReturn(new Document("ok", 1));

        assertDoesNotThrow(() -> validator.validate());

        verify(mongoClient).getDatabase("admin");
        verify(adminDatabase).runCommand(any(Document.class));
    }

    @Test
    void shouldFailStartupValidationWhenPingFails() {

        when(mongoProperties.isValidateConnection())
                .thenReturn(true);

        when(mongoClient.getDatabase("admin"))
                .thenReturn(adminDatabase);

        when(adminDatabase.runCommand(any(Document.class)))
                .thenThrow(new RuntimeException("Mongo unavailable"));

        assertThrows(
                IllegalStateException.class,
                () -> validator.validate());
    }
}
