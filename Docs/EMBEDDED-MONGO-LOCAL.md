# Embedded Mongo — Local Development

Spring Boot 2.3.4 provides Embedded Mongo auto-configuration when
`de.flapdoodle.embed.mongo` is present. Port `0` requests a random free port.

## Local

Run normally:

```bash
mvn spring-boot:run
```

No external MongoDB is required.

## UAT

Use profile `uat` and provide `MONGODB_UAT_URI`.

## PROD

Use profile `prod` and provide `MONGODB_PROD_URI`.

The same MongoRepository/MongoTemplate service code is used in all
environments; only the Mongo connection source changes.

## Removed

The JSON/in-memory UI test layer is intentionally removed.
