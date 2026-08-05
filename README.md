# Enterprise Foundation

Production-oriented Spring Boot 2.7.x foundation application for internal enterprise use. The application provides reusable security, audit, MongoDB infrastructure, Swagger, Actuator, and admin-console foundations so later epics can focus on business functionality.

## Stack

JDK 8 source compatibility, Spring Boot 2.7.x, Spring Security, Spring MVC, Spring Data MongoDB, Mongo Java Driver, MongoTemplate, MongoRepository, Thymeleaf, Swagger UI, Actuator, Maven, Logback, SLF4J, Jackson, JUnit 5, and Mockito.

## Build And Run

```powershell
mvn clean test
mvn package
java -jar target\enterprise-foundation-1.0.0.jar
```

Useful URLs:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/actuator/health`
- `http://localhost:8080/actuator/info`
- `http://localhost:8080/admin`

## Security

Session users authenticate against LDAP and configured roles. API users authenticate through the token endpoint.

```http
POST /api/security/authenticate
Content-Type: application/json

{"username":"svc_api","password":"password"}
```

Successful API authentication returns:

```json
{
  "token": "...",
  "expiresIn": 1800,
  "tokenType": "Bearer"
}
```

Send API tokens as:

```http
Authorization: Bearer <token>
```

Security endpoints:

- `POST /login`
- `POST /logout`
- `POST /api/security/authenticate`
- `POST /api/security/logout`
- `GET /api/security/token`
- `GET /api/security/session`
- `GET /api/security/profile`

Security roles are configured in `application.yml` under `security.roles`. Users are not hardcoded.

## MongoDB

MongoDB infrastructure is fully self-initializing at application startup.

Configuration lives under `mongodb:` in `application.yml`:

- Credentials and authentication database
- Target database
- SSL settings
- Connect and socket timeouts
- One or more server addresses
- Logical collection-name mapping

Startup initialization performs:

- Mongo connectivity validation
- Database accessibility validation
- Idempotent collection creation
- Idempotent index initialization
- Versioned reference-data loading
- Database version tracking in `applicationVersion`
- Startup status reporting through Actuator health

Reference data is stored in:

```text
src/main/resources/reference-data/<version>/*.json
```

Each file declares a target collection, key field, and records. Previously applied versions are skipped automatically.

Example:

```json
{
  "collection": "applicationConfiguration",
  "keyField": "code",
  "records": [
    {
      "code": "ROLE_ADMIN",
      "description": "Administrator role"
    }
  ]
}
```

## Health

`/actuator/health` includes Mongo startup details:

- Mongo status
- Database name
- Initialized collections
- Reference-data version
- Startup status

## Sample APIs

- `GET /api/sample`
- `POST /api/sample`
- `GET /api/admin/sample`
- `GET /api/read/sample`

These endpoints validate controller-service-repository wiring and security enforcement.

## Testing

Run:

```powershell
mvn test
```

The test suite covers security services, token handling, LDAP flow orchestration, filters, audit, exception handling, Mongo startup validation, collection initialization, version comparison, reference-data JSON parsing, and database version management.
