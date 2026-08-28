# UI Part 2 — Logical Table Maintenance

## Scope

Simple Thymeleaf admin maintenance screens for the logical tables already used by FCM:

- Facility Type
- Purpose Code

## Actions

- Add
- Edit
- Save
- Cancel / Reset
- Delete
- Refresh
- Success / Warning / Error messages
- Delete confirmation

## Design

The UI reuses the existing `MaintenanceService` and existing MongoDB repositories.

No React, SPA, new frontend framework or separate API gateway is introduced.

## Delete behavior

The current maintenance entities inherit the `active` field from `BaseEntity`.

The UI uses that existing field for logical deletion:

```text
active = false
```

and the existing GET service returns only active records.

## Edit behavior

Facility Type:

- description
- advised
- committed

Purpose Code:

- description
- unconditionalCancellable

Business keys are immutable during edit.

## Notes

The backend currently exposes update APIs only for indicator changes. The UI uses the existing service/repository layer directly for the admin console so no duplicate REST API contract is introduced.

## Verification

Run:

```bash
mvn clean test
```

Then start the application and navigate to:

```text
/maintenance
```
