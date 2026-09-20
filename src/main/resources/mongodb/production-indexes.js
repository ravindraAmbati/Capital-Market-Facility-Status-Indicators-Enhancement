// CARM-FCM production MongoDB indexes.
//
// auto-index-creation is deliberately disabled in application.yml.
// Run this script through the approved database deployment process
// when application-driven index creation is disabled.
//
// Current collection business key:
// relationshipId + serialNo + facilityNo

db.facilityCapitalMarkers.createIndex(
  {
    relationshipId: 1,
    serialNo: 1,
    facilityNo: 1
  },
  {
    name: "facility_capital_markers_business_key",
    unique: true
  }
);

db.facilityCapitalMarkers.createIndex(
  {
    relationshipId: 1,
    serialNo: 1
  },
  {
    name: "facility_capital_markers_credit_application"
  }
);

db.creditApplicationConsent.createIndex(
  {
    relationshipId: 1,
    serialNo: 1
  },
  {
    name: "credit_application_consent_business_key",
    unique: true
  }
);

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
    timestamp: -1
  },
  {
    name: "api_audit_credit_application_time"
  }
);

db.audit_records.createIndex(
  {
    eventType: 1
  },
  {
    name: "audit_event_type"
  }
);

db.audit_records.createIndex(
  {
    result: 1
  },
  {
    name: "audit_result"
  }
);

db.audit_records.createIndex(
  {
    username: 1
  },
  {
    name: "audit_username"
  }
);

db.applicationVersion.createIndex(
  {
    version: 1
  },
  {
    name: "version",
    unique: true
  }
);

db.facilityTypeMaintenance.createIndex(
  {
    facilityTypeCode: 1
  },
  {
    name: "facility_type_code",
    unique: true
  }
);

db.purposeCodeMaintenance.createIndex(
  {
    purposeCodeHub: 1,
    purposeCodeCarm: 1
  },
  {
    name: "purpose_code_business_key",
    unique: true
  }
);
