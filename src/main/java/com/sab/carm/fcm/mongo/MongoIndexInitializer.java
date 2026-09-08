package com.sab.carm.fcm.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@DependsOn("mongoCollectionInitializer")
public class MongoIndexInitializer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MongoIndexInitializer.class);

    private final MongoTemplate mongoTemplate;

    private final MongoProperties mongoProperties;

    public MongoIndexInitializer(
            MongoTemplate mongoTemplate,
            MongoProperties mongoProperties) {

        this.mongoTemplate = mongoTemplate;
        this.mongoProperties = mongoProperties;
    }

    @PostConstruct
    public void initialize() {

        if (!mongoProperties.isInitializeIndexes()) {

            LOGGER.info(
                    "MongoDB index initialization is disabled.");

            return;
        }

        LOGGER.info("MongoDB index initialization started.");

        for (MongoProperties.CollectionDefinition collection :
                mongoProperties.getDbCollectionNames().asList()) {

            initializeCollectionIndexes(collection);
        }

        LOGGER.info("MongoDB index initialization completed.");
    }

    private void initializeCollectionIndexes(
            MongoProperties.CollectionDefinition collection) {

        if (collection == null
                || collection.getName() == null
                || collection.getName().trim().isEmpty()) {

            return;
        }

        List<MongoProperties.IndexDefinition> definitions =
                collection.getIndexes() == null
                        ? Collections.<MongoProperties.IndexDefinition>emptyList()
                        : collection.getIndexes();

        if (definitions.isEmpty()) {
            return;
        }

        String collectionName = collection.getName();

        if (!mongoTemplate.collectionExists(collectionName)) {

            LOGGER.warn(
                    "Skipping MongoDB index initialization because "
                            + "collection [{}] does not exist.",
                    collectionName);

            return;
        }

        IndexOperations indexOperations =
                mongoTemplate.indexOps(collectionName);

        List<IndexInfo> existingIndexes =
                indexOperations.getIndexInfo();

        for (MongoProperties.IndexDefinition definition :
                definitions) {

            ensureIndex(
                    indexOperations,
                    existingIndexes,
                    definition,
                    collectionName);
        }
    }

    private void ensureIndex(
            IndexOperations indexOperations,
            List<IndexInfo> existingIndexes,
            MongoProperties.IndexDefinition definition,
            String collectionName) {

        if (definition == null
                || definition.getName() == null
                || definition.getName().trim().isEmpty()) {

            return;
        }

        String indexName = definition.getName();

        for (IndexInfo existing : existingIndexes) {

            if (indexName.equals(existing.getName())) {

                LOGGER.info(
                        "MongoDB index [{}] already exists on collection [{}].",
                        indexName,
                        collectionName);

                return;
            }
        }

        if (definition.getFields() == null
                || definition.getFields().isEmpty()) {

            LOGGER.warn(
                    "Skipping MongoDB index [{}] because no fields "
                            + "are configured.",
                    indexName);

            return;
        }

        Index index = new Index();

        for (Map.Entry<String, Integer> field :
                definition.getFields().entrySet()) {

            Direction direction =
                    field.getValue() != null
                            && field.getValue() < 0
                            ? Direction.DESC
                            : Direction.ASC;

            index.on(field.getKey(), direction);
        }

        index.named(indexName);

        if (definition.isUnique()) {
            index.unique();
        }

        indexOperations.ensureIndex(index);

        LOGGER.info(
                "Created MongoDB index [{}] on collection [{}].",
                indexName,
                collectionName);
    }
}
