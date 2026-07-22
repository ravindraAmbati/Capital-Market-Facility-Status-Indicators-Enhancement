# data-design-FacilityCapitalMarkers.md

# Facility Capital Markers Enhancement
## Data Design Document
**Version:** 1.0

---

# 1. Overview

This document describes the MongoDB data model for the **Facility Capital Markers Enhancement**.

The solution stores business data across three independent collections:

| Collection | Purpose |
|------------|---------|
| FacilityCapitalMarkers | Operational data for Facility Capital Markers including version history |
| FacilityCapitalMarkersDecisionConsent | Latest user consent captured for Recommend, Approve and Decline decisions |
| FacilityCapitalMarkersReport | Immutable reporting snapshot generated after every Recommend, Approve and Decline |

---

# 2. Collection : FacilityCapitalMarkers

## Purpose

Stores the latest Facility Capital Marker information.

Whenever a Facility is updated, a new version is created.

Previous version

- isLatest = false

Latest version

- isLatest = true

Deleted facilities are never physically removed unless the Credit Application is permanently deleted.

---

## Document Structure

### Identification

| Field | Description |
|---------|-------------|
| Relationship Id | Relationship Identifier |
| Serial No | Credit Application Serial Number |
| Customer Id | Customer Identifier |
| Borrowing Group Id | Borrowing Group Identifier |
| Underlying System Id | Source System Identifier |
| Facility No | Facility Number |

---

### Facility Details

| Field |
|---------|
| Facility Type |
| Limit Type |
| Purpose Code |

---

### Capital Markers

#### Advised

- Indicator
- Override
- Justification

#### Committed

- Indicator
- Override
- Justification

#### Unconditionally Cancellable

- Indicator
- Override
- Justification

---

### Risk

- Subordinated Flag

---

### Status

| Field |
|---------|
| Credit Application Status |
| Transaction Status |
| Transaction Reason |

---

### Audit Trail

| Field |
|---------|
| Created By |
| Created Date Time |
| Updated By |
| Updated Date Time |

---

### Technical Trail

| Field |
|---------|
| Version |
| Transaction Id |
| isActive |
| isLatest |

---

# Example Document

```json
{
  "identification": {
    "relationshipId": "REL000001",
    "serialNo": "100001",
    "customerId": "CUST001",
    "borrowingGroupId": "BG001",
    "underlyingSystemId": "CLS",
    "facilityNo": "FAC0001"
  },
  "facilityDetails": {
    "facilityType": "Term Loan",
    "limitType": "Funded",
    "purposeCode": "PC001"
  },
  "capitalMarkers": {
    "advised": {
      "indicator": true,
      "override": false,
      "justification": null
    },
    "committed": {
      "indicator": false,
      "override": true,
      "justification": "Business override approved"
    },
    "unconditionallyCancellable": {
      "indicator": false,
      "override": false,
      "justification": null
    }
  },
  "risk": {
    "subordinatedFlag": false
  },
  "status": {
    "creditApplicationStatus": "ACTIVE",
    "transactionStatus": "SUCCESS",
    "transactionReason": null
  },
  "auditTrail": {
    "createdBy": "RM001",
    "createdDateTime": "2026-07-20T09:30:10Z",
    "updatedBy": "RM002",
    "updatedDateTime": "2026-07-21T15:12:25Z"
  },
  "technicalTrail": {
    "version": 3,
    "transactionId": "TXN-100001",
    "isActive": true,
    "isLatest": true
  }
}
```

---

# Version History Example

## Version 1

```json
{
  "technicalTrail":{
      "version":1,
      "isLatest":false,
      "isActive":true
  }
}
```

## Version 2

```json
{
  "technicalTrail":{
      "version":2,
      "isLatest":false,
      "isActive":true
  }
}
```

## Version 3

```json
{
  "technicalTrail":{
      "version":3,
      "isLatest":true,
      "isActive":true
  }
}
```

---

# 3. Collection : FacilityCapitalMarkersDecisionConsent

## Purpose

Stores the latest consent captured before each business decision.

Only one document exists for a Credit Application.

Key

Relationship Id + Serial No

---

## Document Structure

### Identification

| Field |
|---------|
| Relationship Id |
| Serial No |

---

### Decision Consents

| Field |
|---------|
| Recommend |
| Approve |
| Decline |

Each decision contains

| Field |
|---------|
| Action Taken By |
| Action Taken On |

---

### Audit Trail

| Field |
|---------|
| Created By |
| Created Date Time |
| Updated By |
| Updated Date Time |

---

### Technical Trail

| Field |
|---------|
| Transaction Id |

---

# Example Document

```json
{
  "identification": {
    "relationshipId": "REL000001",
    "serialNo": "100001"
  },
  "decisionConsents": {
    "recommend": {
      "actionTakenBy": "RM002",
      "actionTakenOn": "2026-07-21T16:15:00Z"
    },
    "approve": {
      "actionTakenBy": "APR001",
      "actionTakenOn": "2026-07-22T10:45:00Z"
    },
    "decline": null
  },
  "auditTrail": {
    "createdBy": "SYSTEM",
    "createdDateTime": "2026-07-20T09:30:00Z",
    "updatedBy": "APR001",
    "updatedDateTime": "2026-07-22T10:45:00Z"
  },
  "technicalTrail": {
    "transactionId": "TXN-100001"
  }
}
```

---

# 4. Collection : FacilityCapitalMarkersReport

## Purpose

Stores an immutable report snapshot generated after every

- Recommend
- Approve
- Decline

No report is generated for

- Resubmit

---

## Document Structure

### Identification

| Field |
|---------|
| Report Id |
| Relationship Id |
| Serial No |

---

### Credit Application

| Field |
|---------|
| Credit Application Type |
| Credit Application Status |

---

### Customer

| Field |
|---------|
| Customer Id |
| Borrowing Group Id |
| Underlying System Id |

---

### Facility

| Field |
|---------|
| Facility No |
| Facility Type |
| Limit Type |

---

### Proposed Amount

| Field |
|---------|
| Proposed Currency |
| Proposed Amount |
| Proposed Exchange Rate |
| Proposed Amount (SAR) |

---

### Approved Amount

| Field |
|---------|
| Approved Currency |
| Approved Amount |
| Approved Exchange Rate |
| Approved Amount (SAR) |

---

### Capital Markers

#### Advised

- Indicator
- Override
- Justification

#### Committed

- Indicator
- Override
- Justification

#### Unconditionally Cancellable

- Indicator
- Override
- Justification

---

### Additional Details

| Field |
|---------|
| Purpose Code |
| Subordinated Flag |

---

### Decision History

| Field |
|---------|
| Action |
| Action Taken By |
| Action Taken On |

---

### Audit Trail

| Field |
|---------|
| Created By |
| Created Date Time |

---

### Technical Trail

| Field |
|---------|
| Generated Date Time |
| Transaction Id |

---

# Example Document

```json
{
  "identification": {
    "reportId": "REL000001-100001-APR001-20260722104500",
    "relationshipId": "REL000001",
    "serialNo": "100001"
  },
  "creditApplication": {
    "creditApplicationType": "Corporate",
    "creditApplicationStatus": "APPROVED"
  },
  "customer": {
    "customerId": "CUST001",
    "borrowingGroupId": "BG001",
    "underlyingSystemId": "CLS"
  },
  "facility": {
    "facilityNo": "FAC0001",
    "facilityType": "Term Loan",
    "limitType": "Funded"
  },
  "proposedAmount": {
    "currency": "USD",
    "amount": 1000000,
    "exchangeRate": 3.75,
    "amountSAR": 3750000
  },
  "approvedAmount": {
    "currency": "USD",
    "amount": 900000,
    "exchangeRate": 3.75,
    "amountSAR": 3375000
  },
  "capitalMarkers": {
    "advised": {
      "indicator": true,
      "override": false,
      "justification": null
    },
    "committed": {
      "indicator": false,
      "override": true,
      "justification": "Business override approved"
    },
    "unconditionallyCancellable": {
      "indicator": false,
      "override": false,
      "justification": null
    }
  },
  "additionalDetails": {
    "purposeCode": "PC001",
    "subordinatedFlag": false
  },
  "decisionHistory": [
    {
      "action": "RECOMMEND",
      "actionTakenBy": "RM002",
      "actionTakenOn": "2026-07-21T16:15:00Z"
    },
    {
      "action": "APPROVE",
      "actionTakenBy": "APR001",
      "actionTakenOn": "2026-07-22T10:45:00Z"
    }
  ],
  "auditTrail": {
    "createdBy": "SYSTEM",
    "createdDateTime": "2026-07-22T10:45:05Z"
  },
  "technicalTrail": {
    "generatedDateTime": "2026-07-22T10:45:05Z",
    "transactionId": "TXN-100001"
  }
}
```

---

# 5. Design Principles

1. Business data, decision consent and reporting are maintained in separate collections.
2. Facility version is increased only when Facility Capital Marker data changes.
3. Decision consent updates do not create a new Facility version.
4. One Decision Consent document exists per Credit Application.
5. Reports are immutable snapshots generated after Recommend, Approve and Decline.
6. Resubmit updates neither Decision History nor Report collection.
7. Soft delete is implemented using **isActive=false**.
8. Previous versions are maintained using **isLatest=false**.
9. Hard delete is performed only when the Credit Application is permanently deleted.
10. All collections maintain Audit Trail and Technical Trail for traceability.