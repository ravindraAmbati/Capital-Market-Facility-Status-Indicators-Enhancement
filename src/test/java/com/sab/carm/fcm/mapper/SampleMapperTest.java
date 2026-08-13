package com.sab.carm.fcm.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sab.carm.fcm.entity.SampleEntity;
import org.junit.jupiter.api.Test;

class SampleMapperTest {

    @Test
    void mapsEntityMessage() {
        assertThat(new SampleMapper().toResponse(new SampleEntity("ok")).getMessage()).isEqualTo("ok");
    }
}
