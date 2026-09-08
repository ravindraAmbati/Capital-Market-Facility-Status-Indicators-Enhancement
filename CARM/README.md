# CARM - Facility Capital Markers Integration

Target branch:
    feature/FCM-CARM

This ZIP contains the CARM-side integration code only.

Packages:
    com.sab.fcm.carm
        CARM integration/client/cache/service code

    com.sab.carm.fcm
        Reserved for the existing Facility Capital Markers application.
        No FCM business/API code has been invented here because the actual
        FCM REST contract was not supplied.

## Integration

Copy/merge the contents of this ZIP into the existing
FacilityCapitalMarkers application.

The code is intentionally not a new Maven/Spring project.

## Main behavior

1. CARM communicates with Facility Capital Markers through
   CarmFcmMasterService.
2. Only logical reference data is cached.
3. Business data and reports are always requested on demand.
4. Reference data is loaded during CARM startup.
5. Per-JVM locking prevents cache stampede.
6. Soft TTL returns the last known-good value and triggers one
   asynchronous refresh.
7. Hard TTL attempts a synchronous refresh.
8. Scheduled refresh runs at the configured interval.
9. Failed refreshes retain the last known-good data.
10. Retry uses exponential backoff.
11. Notification is exposed through an interface and currently only
    logs the email provision.
12. HIT/MISS metrics are logged periodically.
13. Administrative logical-reference-table maintenance APIs are rejected.

## Required property configuration

Copy:
    carm-fcm.properties.example

to the existing application's classpath as:
    carm-fcm.properties

Use the actual Facility Capital Markers API paths.

Credentials:
    username = sa-svc-carm-fcm-api
    password = encrypted using Jasypt

The Jasypt master password should be maintained as the IBM WAS JVM
Custom Property:
    carm.fcm.jasypt.password

Do not put the Jasypt master password into source control.

## Existing Spring XML

If the existing application already has component scanning, import/use
CarmFcmConfiguration rather than creating another application context.

If the application already owns a RestTemplate, executor, or scheduling
configuration, those can be wired into this code instead of creating
duplicates.

The configuration currently uses Spring 4.2-compatible RestTemplate and
Spring scheduling.

## Dependencies expected

Existing application should provide:
    Spring Context 4.2.4.RELEASE
    Spring Web 4.2.4.RELEASE
    Jackson
    Apache HttpClient
    Jasypt 1.9.3

Tests expect:
    JUnit 4.12
    Mockito
    Spring Test

## Important: actual FCM REST contract

The REST client is generic and supports:
    GET
    POST
    PUT
    DELETE

It deliberately does not hard-code guessed FCM endpoint names.

Before UAT validation, replace the placeholders in
carm-fcm.properties with the actual Facility Capital Markers endpoints.

## Live integration

Unit tests do not contact an external FCM server.

For the independent live test, point the properties at UAT and run an
explicit integration test against the real Facility Capital Markers
application. Do not point developer tests at PROD.

## Build

From the existing FacilityCapitalMarkers project:

    mvn clean test

No new project is created by this ZIP.
