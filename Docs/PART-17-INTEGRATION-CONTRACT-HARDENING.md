# Part 17 — Integration Contract Hardening

## Objective

Make CARM-FCM response tracing consistent across all integration endpoints.

## Response headers

Every successful CARM-FCM JSON/CSV response exposes:

- `X-CARM-FCM-CorrelationId`
- `X-CARM-FCM-TransactionId`

The correlation ID is the value supplied by CARM.

The transaction ID is generated once by `CorrelationIdFilter` for the request and reused through the transaction context.

## UI boundary

The mandatory correlation filter remains limited to:

- `/api/carm/fcm/**`
- `/api/carm/reference-data/**`

UI, maintenance and security APIs are not forced to provide the CARM integration correlation header.

## Error responses

Integration error responses also expose the tracing headers when the request reaches the integration layer.

## JDK compatibility

Implementation uses JDK 8-compatible APIs only.

## Tests

Part 17 adds unit coverage for the response-header factory.

Run:

```bash
mvn clean test
```

No real MongoDB is required.
