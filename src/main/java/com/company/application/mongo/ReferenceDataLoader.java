package com.company.application.mongo;

import com.company.application.exception.MongoInfrastructureException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Loads versioned reference data from classpath JSON files.
 */
@Component
public class ReferenceDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataLoader.class);

    private final ReferenceDataJsonParser parser;
    private final DatabaseVersionManager versionManager;
    private final VersionComparator versionComparator;

    public ReferenceDataLoader(ReferenceDataJsonParser parser, DatabaseVersionManager versionManager,
            VersionComparator versionComparator) {
        this.parser = parser;
        this.versionManager = versionManager;
        this.versionComparator = versionComparator;
    }

    public void load(MongoTemplate mongoTemplate, MongoStartupStatus status) {
        for (VersionFolder versionFolder : discoverVersionFolders()) {
            if (versionManager.isApplied(versionFolder.getVersion())) {
                LOGGER.info("Reference data version already applied version={}", versionFolder.getVersion());
                continue;
            }
            loadVersion(mongoTemplate, versionFolder);
            versionManager.markSuccessful(versionFolder.getVersion());
            status.setReferenceDataVersion(versionFolder.getVersion());
            LOGGER.info("Reference data version applied version={}", versionFolder.getVersion());
        }
    }

    private void loadVersion(MongoTemplate mongoTemplate, VersionFolder versionFolder) {
        for (Resource resource : versionFolder.getResources()) {
            try (InputStream inputStream = resource.getInputStream()) {
                ReferenceDataFile file = parser.parse(inputStream);
                for (Document record : file.getRecords()) {
                    Object key = record.get(file.getKeyField());
                    Query query = new Query(Criteria.where(file.getKeyField()).is(key));
                    if (!mongoTemplate.exists(query, file.getCollection())) {
                        mongoTemplate.insert(record, file.getCollection());
                    }
                }
            } catch (IOException ex) {
                throw new MongoInfrastructureException("Unable to read reference-data resource", ex);
            }
        }
    }

    private List<VersionFolder> discoverVersionFolders() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:reference-data/*/*.json");
            java.util.Map<String, List<Resource>> byVersion = new java.util.TreeMap<>(versionComparator::compare);
            for (Resource resource : resources) {
                String url = resource.getURL().toString().replace("\\", "/");
                String marker = "/reference-data/";
                int start = url.indexOf(marker);
                if (start >= 0) {
                    String remainder = url.substring(start + marker.length());
                    String version = remainder.substring(0, remainder.indexOf('/'));
                    byVersion.computeIfAbsent(version, ignored -> new ArrayList<>()).add(resource);
                }
            }
            List<VersionFolder> folders = new ArrayList<>();
            for (java.util.Map.Entry<String, List<Resource>> entry : byVersion.entrySet()) {
                entry.getValue().sort(Comparator.comparing(Resource::getFilename));
                folders.add(new VersionFolder(entry.getKey(), entry.getValue()));
            }
            return folders;
        } catch (IOException ex) {
            throw new MongoInfrastructureException("Unable to discover reference-data files", ex);
        }
    }

    private static class VersionFolder {
        private final String version;
        private final List<Resource> resources;
        VersionFolder(String version, List<Resource> resources) {
            this.version = version;
            this.resources = resources;
        }
        String getVersion() { return version; }
        List<Resource> getResources() { return resources; }
    }
}
