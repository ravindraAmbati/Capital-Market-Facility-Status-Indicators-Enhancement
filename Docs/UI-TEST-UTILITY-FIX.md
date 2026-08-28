# UI Test Utility — Corrected Design

## Required runtime paths

Local test mode:

```text
UI Controller
    ↓
MaintenanceService
    ↓
UiTestDataStore
    ↓
src/main/resources/mongodb/test-*.json
```

Normal mode:

```text
UI Controller
    ↓
MaintenanceService
    ↓
Mongo Repository
    ↓
MongoDB
```

The UI controller must not inject Mongo repositories.

## Important

`UiMaintenanceDataService` is no longer required by this design. The service switch belongs in `MaintenanceService`.

The test-data mode is disabled by default:

```text
fcm.ui.test-data.enabled=false
```

Enable for local UI testing:

```bash
mvn spring-boot:run ^
  -Dfcm.ui.test-data.enabled=true ^
  -Dfcm.ui.test-data.scenario=standard
```

Linux/macOS:

```bash
mvn spring-boot:run   -Dfcm.ui.test-data.enabled=true   -Dfcm.ui.test-data.scenario=standard
```

No MongoDB read/write is used by the maintenance UI while test mode is enabled.

## JSON scenarios

- `test-standard.json` — normal data
- `test-empty.json` — no active records
- `test-duplicates.json` — existing keys for duplicate validation
- `test-error.json` — basic error-path data

## CRUD behavior

Test mode supports the same UI actions:

- Add
- Edit
- Save
- Delete
- Reset/refresh through normal page navigation

Changes are held in memory and are lost when the application restarts.

## Production safety

Test mode must remain disabled in UAT/PROD.

Do not use test fixtures as production data.
