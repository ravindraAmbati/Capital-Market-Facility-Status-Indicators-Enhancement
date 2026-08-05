package com.company.application.repository;

import com.company.application.entity.ApplicationVersion;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for database version history.
 */
public interface ApplicationVersionRepository extends MongoRepository<ApplicationVersion, String> {

    Optional<ApplicationVersion> findFirstByStatusOrderByDatabaseVersionDesc(String status);
    boolean existsByDatabaseVersion(String version);
}
