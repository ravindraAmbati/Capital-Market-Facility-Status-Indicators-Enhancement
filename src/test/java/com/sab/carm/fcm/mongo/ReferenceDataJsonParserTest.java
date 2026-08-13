package com.sab.carm.fcm.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sab.carm.fcm.exception.MongoInfrastructureException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ReferenceDataJsonParserTest {

    @Test
    void parsesValidReferenceData() {
        String json = "{\"collection\":\"config\",\"keyField\":\"code\",\"records\":[{\"code\":\"A\"}]}";

        ReferenceDataFile file = new ReferenceDataJsonParser(new ObjectMapper())
                .parse(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        assertThat(file.getCollection()).isEqualTo("config");
        assertThat(file.getRecords()).hasSize(1);
    }

    @Test
    void rejectsInvalidReferenceData() {
        String json = "{\"collection\":\"config\"}";

        assertThatThrownBy(() -> new ReferenceDataJsonParser(new ObjectMapper())
                .parse(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))))
                .isInstanceOf(MongoInfrastructureException.class);
    }
}
