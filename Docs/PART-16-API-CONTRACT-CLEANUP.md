# Part 16 — API Contract Cleanup

## Objective

Make Swagger/OpenAPI the authoritative API contract without changing the agreed CARM-FCM behavior.

## Canonical defaults API

GET `/api/carm/fcm/defaults`

`X-CARM-FCM-CorrelationId` is mandatory because this is a CARM-FCM integration API.

## Changes

- Added explicit Swagger metadata to the canonical DefaultsController.
- Documented the integration correlation header.
- Documented 200/400/401/403/500 responses.
- Clarified that FCM exposes maintenance data and CARM calculates defaults.
- Removed the duplicate defaults-controller implementation.

## Duplicate implementation to delete

The branch contains two controllers for the same endpoint. Keep:

`DefaultsController`

Delete:

`FacilityCapitalMarkersDefaultsController.java`

and its obsolete test:

`FacilityCapitalMarkersDefaultsControllerTest.java`

## No API behavior change

No changes to endpoint paths, HTTP methods, DTOs, MongoDB behavior, CARM default calculation, or authentication behavior.

## Compatibility

JDK 8, Spring Boot 2.3.4.RELEASE and springdoc-openapi-ui 1.7.0.
No POM change is required.
