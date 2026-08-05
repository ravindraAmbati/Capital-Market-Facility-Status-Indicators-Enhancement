package com.company.application.mongo;

import com.company.application.entity.ApplicationVersion;
import com.company.application.repository.ApplicationVersionRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Reads and records applied database versions.
 */
@Component
public class DatabaseVersionManager {

    private static final String SUCCESS = "SUCCESS";

    private final ApplicationVersionRepository repository;

    public DatabaseVersionManager(ApplicationVersionRepository repository) {
        this.repository = repository;
    }

    public Optional<String> currentVersion() {
        return repository.findFirstByStatusOrderByDatabaseVersionDesc(SUCCESS).map(ApplicationVersion::getDatabaseVersion);
    }

    public boolean isApplied(String version) {
        return repository.existsByDatabaseVersion(version);
    }

    public void markSuccessful(String version) {
        repository.save(new ApplicationVersion(version, SUCCESS));
    }
}
