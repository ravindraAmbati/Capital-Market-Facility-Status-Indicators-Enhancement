# CARM Facility Capital Markers (FCM)

## Technology Stack

| Component | Version |
|-----------|----------|
| Java | JDK 8 |
| Spring Boot | 2.3.4.RELEASE |
| Spring Data MongoDB | 3.0.4.RELEASE |
| MongoDB Java Driver | 4.0.5 |
| Maven | 3.x |
| Swagger | SpringDoc |
| Thymeleaf | Latest compatible |
| LDAP | Active Directory |

---

# Running the Application

```
mvn clean spring-boot:run
```

Application URL

```
http://localhost:8080
```

Swagger

```
http://localhost:8080/swagger-ui.html
```

Health Check

```
http://localhost:8080/actuator/health
```

---

# MongoDB Configuration

MongoDB connection is configured using Spring Boot standard property.

```
spring.data.mongodb.uri
```

Example

```
mongodb://username:password@host1:27017,host2:27017,host3:27017,host4:27017/carm_fcm?replicaSet=rs0&authSource=admin&ssl=true
```

Collections are configured separately.

```
mongodb:
  collections:
```

This allows changing collection names without changing Java code.

---

# Encrypting MongoDB Credentials

The application uses **Jasypt** to encrypt sensitive properties.

Dependency

```xml
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
```

---

## Generate Encryption Password

Choose a strong encryption password.

Example

```
FacilityCapitalMarkers@2026
```

Do **NOT** commit this password to GitHub.

---

## Encrypt MongoDB URI

Plain Text

```
mongodb://username:password@host1:27017,host2:27017,host3:27017,host4:27017/carm_fcm?replicaSet=rs0&authSource=admin&ssl=true
```

Encrypt using Jasypt CLI or utility.

Example Output

```
QJ8as8df8A7sd89asd9asd89asd==
```

Update application.yml

```yaml
spring:
  data:
    mongodb:
      uri: ENC(QJ8as8df8A7sd89asd9asd89asd==)
```

---

# Providing Encryption Password

## IntelliJ

VM Options

```
-Djasypt.encryptor.password=FacilityCapitalMarkers@2026
```

---

## Windows

```
set JASYPT_ENCRYPTOR_PASSWORD=FacilityCapitalMarkers@2026
```

---

## Linux

```
export JASYPT_ENCRYPTOR_PASSWORD=FacilityCapitalMarkers@2026
```

---

## Kubernetes

Use Secret

```
JASYPT_ENCRYPTOR_PASSWORD
```

---

# Decryption

No Java code is required.

During application startup,

```
ENC(...)
```

is automatically decrypted by Jasypt before Spring creates the MongoDB connection.

---

# Collections

Current Collections

| Collection |
|------------|
| facilityCapitalMarkers |
| facilityCapitalMarkersDecisionHistory |
| creditApplicationCapitalMarkersReport |
| applicationAuditLog |

Collections are **NOT** automatically created.

Indexes are **NOT** automatically created.

```
spring.data.mongodb.auto-index-creation=false
```

---

# Security Roles

| Role | Service Account |
|------|-----------------|
| Admin | sa-svc-carm-fcm-admin |
| API | sa-svc-carm-fcm-api |
| Audit | sa-svc-carm-fcm-audit |
| IT Support | sa-svc-carm-fcm-itsup |

---

# Notes

- Never commit plain text credentials.
- Never commit encryption passwords.
- Store the Jasypt encryption password outside the application.
- Use different encrypted MongoDB URIs for DEV, SIT, UAT and PROD.
- Keep collection names configurable through `application.yml`.

---

# MongoDB Collections

The application uses the following MongoDB collections.

| Collection | Purpose |
|------------|---------|
| facilityCapitalMarkers | Stores the latest working copy of Facility Capital Marker information. |
| facilityCapitalMarkersDecisionHistory | Stores the complete decision history (Recommend, Approve and Decline). |
| creditApplicationCapitalMarkersReport | Stores immutable Credit Application report snapshots generated during Recommend, Approve and Decline. |
| applicationAuditLog | Stores application audit logs including login, logout, authentication, authorization, API requests, exceptions and technical audit information. |
| facilityTypeMappings | Stores Facility Type mappings maintained through the Admin Console. |
| limitTypeMappings | Stores Limit Type mappings maintained through the Admin Console. |
| purposeCodeMappings | Stores Purpose Code mappings maintained through the Admin Console. |

---

## Collection Usage

### facilityCapitalMarkers

Stores the latest version of Facility Capital Marker information.

Contains

- Relationship Id
- Serial No
- Facility No
- Capital Marker Indicators
- Override
- Justification
- Purpose Code
- Subordinate Flag
- Version
- isLatest
- isActive
- Audit Trail
- Technical Trail

---

### facilityCapitalMarkersDecisionHistory

Stores all Credit Application decisions.

Supported Decisions

- Recommend
- Approve
- Decline

Contains

- Relationship Id
- Serial No
- Action
- Action Taken By
- Action Taken On
- Audit Trail
- Technical Trail

---

### creditApplicationCapitalMarkersReport

Stores immutable report snapshots.

Generated During

- Recommend
- Approve
- Decline

Contains

- Credit Application
- All Facilities
- Decision History
- Audit Trail
- Technical Trail

Reports are never updated after generation.

---

### applicationAuditLog

Stores application level audit information.

Includes

- Login
- Logout
- Authentication
- Authorization
- API Requests
- API Responses
- Exceptions
- Client IP Address
- Transaction Id
- Correlation Id
- Execution Time

---

### facilityTypeMappings

Stores Facility Type mappings synchronized with the Existing CARM application.

Maintained through the Admin Console.

---

### limitTypeMappings

Stores Limit Type mappings synchronized with the Existing CARM application.

Maintained through the Admin Console.

---

### purposeCodeMappings

Stores Purpose Code mappings synchronized with the Existing CARM application.

Maintained through the Admin Console.

---

## Collection Creation

The application **does not** automatically create MongoDB collections.

The application **does not** automatically create MongoDB indexes.

Collections must be created by the MongoDB DBA before deployment.

```
spring.data.mongodb.auto-index-creation=false
```

---

## Naming Convention

| Type | Naming Convention |
|------|-------------------|
| Collection | camelCase |
| Document | PascalCase |
| Java Class | PascalCase |
| Java Field | camelCase |
| Mongo Field | camelCase |

Example

| Java Class | MongoDB Collection |
|------------|-------------------|
| FacilityCapitalMarkers | facilityCapitalMarkers |
| FacilityCapitalMarkersDecisionHistory | facilityCapitalMarkersDecisionHistory |
| CreditApplicationCapitalMarkersReport | creditApplicationCapitalMarkersReport |
| ApplicationAuditLog | applicationAuditLog |
| FacilityTypeMapping | facilityTypeMappings |
| LimitTypeMapping | limitTypeMappings |
| PurposeCodeMapping | purposeCodeMappings |