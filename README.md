# Enterprise Foundation

Production-oriented Spring Boot 2.7.x foundation application for internal enterprise use.

## Stack

JDK 8 source compatibility, Spring Boot 2.7.x, Spring Security, Spring MVC, Spring Data MongoDB, Thymeleaf, Swagger UI, Actuator, Maven, Logback, SLF4J, JUnit 5, and Mockito.

## Run

```powershell
mvn clean test
mvn spring-boot:run
```

Configure MongoDB and LDAP through `src/main/resources/application.yml` or environment variables.

Useful URLs:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/actuator/health`
- `http://localhost:8080/actuator/info`
- `http://localhost:8080/admin`

## Authentication

Session users authenticate through HTTP Basic against LDAP and configured roles.

API users call:

```http
POST /api/security/authenticate
Content-Type: application/json

{"username":"svc_api","password":"password"}
```

Then send `Authorization: Bearer <token>` on API requests.
