package com.company.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.company.application.entity.SampleEntity;
import org.junit.jupiter.api.Test;

class SampleMapperTest {

    @Test
    void mapsEntityMessage() {
        assertThat(new SampleMapper().toResponse(new SampleEntity("ok")).getMessage()).isEqualTo("ok");
    }
}
