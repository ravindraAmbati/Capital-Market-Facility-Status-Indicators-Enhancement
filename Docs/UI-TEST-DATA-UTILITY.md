# UI Test Data Utility — No MongoDB Required

## Purpose

The FCM UI maintenance screens can be tested without a running MongoDB instance.

The utility uses JSON fixtures under:

```text
src/main/resources/mongodb/test-*.json
```

The test store exists only for local UI testing and is disabled by default.

## Enable

Run the application with:

```bash
mvn spring-boot:run   -Dfcm.ui.test-data.enabled=true   -Dfcm.ui.test-data.scenario=standard
```

or place the following in a local-only configuration:

```yaml
fcm:
  ui:
    test-data:
      enabled: true
      scenario: standard
      directory: classpath:/mongodb/
```

## Scenarios

```text
test-standard.json
    Normal maintenance data

test-empty.json
    No active records

test-duplicates.json
    Existing keys for duplicate/add validation

test-error.json
    Error-message test data
```

## Runtime behavior

When enabled:

```text
Browser
  ↓
Thymeleaf Maintenance UI
  ↓
UiMaintenanceDataService
  ↓
UiTestDataStore
  ↓
test-<scenario>.json loaded into memory
```

No MongoDB read or write is needed for UI maintenance testing.

CRUD changes exist only in memory and disappear when the application restarts or the fixture is reset.

When disabled:

```text
Browser
  ↓
MaintenanceUiController
  ↓
existing MaintenanceService / repository path
  ↓
MongoDB
```

The production path is unchanged.

## Reset

```http
POST /maintenance/test-data/reset
```

or use the Reset Test Data action if exposed in the UI.

## Status

```http
GET /maintenance/test-data/status
```

returns whether test-data mode is enabled and the active scenario.

## Important

Do not enable this mode in UAT or production.

The utility is specifically for local/manual UI testing when MongoDB is unavailable.
