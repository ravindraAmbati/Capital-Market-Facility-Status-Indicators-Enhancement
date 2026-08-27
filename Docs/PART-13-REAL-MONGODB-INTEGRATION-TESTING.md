# Part 13 — Real MongoDB Integration Testing

Real MongoDB tests are named `*IT.java` and are excluded from the normal Surefire test run.

## Normal development

```bash
mvn clean test
```

Does not execute the integration tests and does not require a real MongoDB instance.

Starting the application also does not execute integration tests:

```bash
mvn spring-boot:run
```

## Explicit integration run

Use a dedicated integration-test database:

```bash
mvn clean verify -DMONGODB_IT_URI="mongodb://user:password@host:27017/fcm_it"
```

Failsafe executes `**/*IT.java`.

## Coverage

Part 13 verifies against real MongoDB:

- create current record
- idempotent identical POST -> `NO_CHANGE`
- changed POST -> previous current record in history and new current record
- DELETE -> history plus physical deletion of current
- deleted facility number reuse
- `<facilityNo>_DELETED_<correlationId>`
- `originalFacilityNo` preservation

No production-only test switch is added and no integration test runs during application startup.

## Maven

Add `POM-PART-13-SNIPPET.xml` inside the existing `<build><plugins>` section.
No new runtime dependency is required.
