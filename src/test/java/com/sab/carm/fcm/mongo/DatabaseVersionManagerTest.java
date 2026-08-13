package com.sab.carm.fcm.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sab.carm.fcm.entity.ApplicationVersion;
import com.sab.carm.fcm.repository.ApplicationVersionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DatabaseVersionManagerTest {

    @Test
    void readsCurrentSuccessfulVersion() {
        ApplicationVersionRepository repository = Mockito.mock(ApplicationVersionRepository.class);
        when(repository.findFirstByStatusOrderByDatabaseVersionDesc("SUCCESS"))
                .thenReturn(Optional.of(new ApplicationVersion("1.0.0", "SUCCESS")));

        assertThat(new DatabaseVersionManager(repository).currentVersion()).contains("1.0.0");
    }

    @Test
    void marksVersionSuccessful() {
        ApplicationVersionRepository repository = Mockito.mock(ApplicationVersionRepository.class);

        new DatabaseVersionManager(repository).markSuccessful("1.0.0");

        verify(repository).save(Mockito.any(ApplicationVersion.class));
    }
}
