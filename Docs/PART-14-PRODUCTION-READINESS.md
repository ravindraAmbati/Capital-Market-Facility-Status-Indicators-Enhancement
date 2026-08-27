# Part 14 — Production Readiness

## Scope

Part 14 is the production-readiness pass for the CARM–FCM integration.

Part 13 (real MongoDB end-to-end testing) is intentionally parked and is **not** replaced by this part.

## Completed in this part

- Production-oriented Spring configuration
- MongoDB automatic index creation remains disabled
- Explicit MongoDB index deployment script
- Actuator health details restricted to authorized users
- Environment information disabled from `/actuator/info`
- Thymeleaf production caching enabled
- Application package logging reduced from DEBUG to INFO
- No report collection configured
- No report persistence introduced

## MongoDB index deployment

`src/main/resources/mongodb/production-indexes.js` must be executed by the approved database deployment process.

Do not enable:

```yaml
spring:
  data:
    mongodb:
      auto-index-creation: true
```

in production.

## Important production gate

Part 13 is still pending:

- real MongoDB integration tests
- facility create/update/delete/re-use lifecycle
- consent persistence
- real-time report extraction
- API audit persistence
- transaction/correlation trace verification

Therefore Part 14 completion does **not** mean production deployment is approved.

## Final pre-production checklist

- [ ] Part 13 completed against dedicated test MongoDB
- [ ] Production MongoDB indexes deployed/verified
- [ ] LDAP connectivity verified
- [ ] CARM service account/role verified
- [ ] `X-CARM-FCM-CorrelationId` verified as mandatory
- [ ] FCM transaction ID propagation verified
- [ ] API audit records verified
- [ ] Facility number reuse scenario verified
- [ ] No report collection exists
- [ ] Secrets supplied through approved secret management
- [ ] Swagger access verified
- [ ] Actuator access verified
- [ ] Production logging level verified
- [ ] Backup/restore procedure verified
- [ ] Rollback procedure approved
