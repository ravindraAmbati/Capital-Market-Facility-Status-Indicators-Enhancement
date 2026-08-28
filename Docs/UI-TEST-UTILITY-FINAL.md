# UI Test Utility — Final

## Architecture

### MongoDB available

```text
Thymeleaf UI
    ↓
MaintenanceUiController
    ↓
MaintenanceService
    ↓
Mongo Repository
    ↓
MongoDB
```

### MongoDB unavailable

```text
Thymeleaf UI
    ↓
MaintenanceUiController
    ↓
MaintenanceService
    ↓
UiTestDataStore
    ↓
src/main/resources/mongodb/test-*.json
```

The controller does not inject Mongo repositories.

## Enable local JSON mode

```bash
mvn spring-boot:run ^
  -Dfcm.ui.test-data.enabled=true ^
  -Dfcm.ui.test-data.scenario=standard
```

Linux/macOS:

```bash
mvn spring-boot:run   -Dfcm.ui.test-data.enabled=true   -Dfcm.ui.test-data.scenario=standard
```

## Scenarios

- `test-standard.json` — normal records
- `test-empty.json` — empty lists
- `test-duplicates.json` — duplicate-key scenarios
- `test-error.json` — data for negative/error-path testing

## Runtime behavior

The service decides which data source is used.

```text
fcm.ui.test-data.enabled=true
    → JSON / in-memory

fcm.ui.test-data.enabled=false
    → MongoDB
```

The JSON mode does not write anything to MongoDB.

Changes made through the UI are in memory and are lost when the application restarts.

## Existing tests

The three-argument `MaintenanceService` constructor is intentionally preserved so the existing unit tests continue to compile without modification.

Run:

```bash
mvn clean test
```

## Production safety

Keep:

```text
fcm.ui.test-data.enabled=false
```

in UAT and production.
