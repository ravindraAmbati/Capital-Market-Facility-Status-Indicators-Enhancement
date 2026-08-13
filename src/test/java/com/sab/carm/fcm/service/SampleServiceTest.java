package com.sab.carm.fcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.entity.SampleEntity;
import com.sab.carm.fcm.repository.SampleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SampleServiceTest {

    @Test
    void createsSampleThroughRepository() {
        SampleRepository repository = Mockito.mock(SampleRepository.class);
        when(repository.save(any(SampleEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        SampleService service = new SampleService(repository);

        assertThat(service.createSample().getMessage()).isEqualTo("sample write successful");
        verify(repository).save(any(SampleEntity.class));
    }
}
