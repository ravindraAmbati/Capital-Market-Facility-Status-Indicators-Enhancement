# Part 15 — Swagger / OpenAPI

## Objective

Swagger/OpenAPI is the authoritative API contract for the FCM REST APIs.

README.md should describe architecture, implementation, deployment, operations and project status instead of duplicating every API request and response.

## Correlation ID rule

`X-CARM-FCM-CorrelationId` is mandatory only for CARM-FCM integration APIs:

- `/api/carm/fcm/**`
- `/api/carm/reference-data/**`

It is NOT mandatory for:

- `/api/maintenance/**`
- `/api/security/**`
- `/login`
- `/logout`

## Swagger endpoints

Swagger UI:

```text
/swagger-ui.html
```

OpenAPI document:

```text
/v3/api-docs
```

## Security

Protected APIs use the `bearerAuth` OpenAPI security scheme.

## Documentation generated from code

The OpenAPI contract is built from:

- controller mappings
- request/response DTOs
- OpenAPI configuration
- common integration headers
- security configuration

`swagger-ui.html` itself is not modified.

## Part 15 scope

- Correct integration-only correlation header
- Bearer authentication scheme
- Common integration error responses
- API grouping foundation
- Runtime-generated request/response schemas
- Swagger/OpenAPI documentation foundation
