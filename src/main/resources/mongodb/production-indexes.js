// CARM-FCM production MongoDB indexes.
//
// auto-index-creation is deliberately disabled in application.yml.
// Run this script through the approved database deployment process.
//
// Current collection business key:
// relationshipId + serialNo + facilityNo
//
// The current FacilityCapitalMarkers entity declares the same
// compound unique index.

db.facilityCapitalMarkers.createIndex(
  {
    creditApplicationRelationshipId: 1,
    serialNo: 1,
    facilityNo: 1
  },
  {
    name: "facility_capital_markers_business_key",
    unique: true
  }
);

// Credit application report reads facilities by relationshipId + serialNo.
db.facilityCapitalMarkers.createIndex(
  {
    creditApplicationRelationshipId: 1,
    serialNo: 1
  },
  {
    name: "facility_capital_markers_credit_application"
  }
);

// Consent is stored at credit-application level.
db.creditApplicationConsent.createIndex(
  {
    relationshipId: 1,
    serialNo: 1
  },
  {
    name: "credit_application_consent_business_key"
  }
);

// API audit is primarily queried for production support tracing.
db.apiAudit.createIndex(
  {
    correlationId: 1,
    transactionId: 1
  },
  {
    name: "api_audit_trace"
  }
);

db.apiAudit.createIndex(
  {
    relationshipId: 1,
    serialNo: 1,
    createdDate: -1
  },
  {
    name: "api_audit_credit_application_time"
  }
);
