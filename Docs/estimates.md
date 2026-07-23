# Epic Estimates

## Project Overview

This document provides the high-level implementation estimates for the **Facility Capital Markers Enhancement** project.

The estimates are based on:

- JDK 8
- Spring Boot 2.x
- Spring MVC
- Spring Data MongoDB
- Thymeleaf UI
- Swagger UI
- Logback
- Existing MongoDB
- Existing Kong API Gateway
- Existing CARM Integration
- CSV Report Generation
- One experienced Java Developer (8–10 Years)
- Medium complexity enterprise banking enhancement

---

# Estimation Summary

| Activity | Person Days |
|-----------|------------:|
| Development | **79** |
| Unit + API Testing | **29** |
| SIT | **17** |
| **Grand Total** | **125 Person Days** |

---

# Epic 1 – Administration & Reference Data Management

## Objective

Provide an administration portal for maintaining application configuration and reference data.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US1.1 | Create Administration module | Admin module |
| US1.2 | Develop Thymeleaf Administration UI | Configuration screens |
| US1.3 | Create Facility Type Mapping | CRUD functionality |
| US1.4 | Create Limit Type Mapping | CRUD functionality |
| US1.5 | Create Purpose Code Mapping | CRUD functionality |
| US1.6 | Create Capital Marker Mapping | CRUD functionality |
| US1.7 | Search Configuration | Search functionality |
| US1.8 | View Configuration | View page |
| US1.9 | Update Configuration | Updated configuration |
| US1.10 | Delete Configuration | Soft deleted configuration |
| US1.11 | Capture Audit Trail | Audit information |

### Deliverables

- Thymeleaf UI
- Admin Configuration APIs
- CRUD Operations
- Search
- Audit Trail

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 6 |
| Unit + API Testing | 2 |
| SIT | 1 |
| **Total** | **9** |

---

# Epic 2 – Reference Data Synchronization

## Objective

Synchronize reference data between Existing CARM and the Facility Capital Markers application.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US2.1 | Retrieve Facility Types | Facility Types |
| US2.2 | Retrieve Limit Types | Limit Types |
| US2.3 | Retrieve Purpose Codes | Purpose Codes |
| US2.4 | Compare Reference Data | Difference Report |
| US2.5 | Detect Unmapped Values | Unmapped Records |
| US2.6 | Create Hourly Synchronization Batch | Batch Job |
| US2.7 | Log Synchronization Results | Batch Logs |
| US2.8 | Handle Synchronization Errors | Error Handling |

### Deliverables

- Synchronization Service
- Hourly Batch
- Reference Data APIs
- Batch Logs

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 5 |
| Unit + API Testing | 2 |
| SIT | 1 |
| **Total** | **8** |

---

# Epic 3 – Facility Capital Marker Management

## Objective

Manage Facility Capital Marker operational data.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US3.1 | Create Facility | Facility Record |
| US3.2 | Update Facility | Updated Facility |
| US3.3 | View Facility | Facility Details |
| US3.4 | Search Facilities | Search Results |
| US3.5 | Cancel Facility | Soft Deleted Facility |
| US3.6 | Restore Facility | Active Facility |
| US3.7 | Permanently Delete Facility | Deleted Facility |
| US3.8 | Determine Capital Markers | Marker Values |
| US3.9 | Apply Override Logic | Updated Markers |
| US3.10 | Capture Justification | Stored Comments |
| US3.11 | Maintain Version History | Version Records |
| US3.12 | Maintain Audit Trail | Audit Information |
| US3.13 | Maintain Technical Trail | Technical Metadata |

### Deliverables

- Facility APIs
- Capital Marker Engine
- Version Management
- MongoDB Collection

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 18 |
| Unit + API Testing | 6 |
| SIT | 3 |
| **Total** | **27** |

---

# Epic 4 – Validation Framework

## Objective

Validate Facilities and Credit Applications before business decisions.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US4.1 | Validate Facility | Validation Result |
| US4.2 | Validate Credit Application | Validation Result |
| US4.3 | Validate Mapping Rules | Validation Result |
| US4.4 | Validate Overrides | Validation Result |
| US4.5 | Validate Mandatory Fields | Validation Result |
| US4.6 | Return Validation Errors | Error Messages |
| US4.7 | Return Validation Warnings | Warning Messages |

### Deliverables

- Validation APIs
- Validation Rules
- Error Handling

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 8 |
| Unit + API Testing | 3 |
| SIT | 2 |
| **Total** | **13** |

---

# Epic 5 – Existing CARM Integration

## Objective

Integrate the application with Existing CARM.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US5.1 | Integrate Facility APIs | Facility Integration |
| US5.2 | Integrate Decision APIs | Decision Integration |
| US5.3 | Integrate Validation APIs | Validation Integration |
| US5.4 | Retrieve Facility Information | Facility Data |
| US5.5 | Retrieve Financial Information | Financial Data |
| US5.6 | Retrieve Customer Information | Customer Data |
| US5.7 | Support CSV Report Download | CSV Download |
| US5.8 | Add Three New Dropdowns | Updated CARM UI |
| US5.9 | Handle Integration Failures | Error Handling |

### Deliverables

- Existing CARM Integration
- API Integration
- CSV Download Support

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 11 |
| Unit + API Testing | 4 |
| SIT | 3 |
| **Total** | **18** |

---

# Epic 6 – Credit Application Decision Processing

## Objective

Process Credit Application business decisions.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US6.1 | Process Recommend | Updated Status |
| US6.2 | Process Resubmit | Updated Status |
| US6.3 | Process Approve | Updated Status |
| US6.4 | Process Decline | Updated Status |
| US6.5 | Capture Decision History | Decision History |
| US6.6 | Trigger Report Generation | Report Request |
| US6.7 | Update Transaction Status | Transaction Update |

### Deliverables

- Decision APIs
- Decision Processing
- Decision History

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 8 |
| Unit + API Testing | 3 |
| SIT | 2 |
| **Total** | **13** |

---

# Epic 7 – Credit Application Reporting (CSV)

## Objective

Generate Credit Application level CSV reports.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US7.1 | Generate Credit Application Report | Report Snapshot |
| US7.2 | Retrieve Facility Information | Facility Data |
| US7.3 | Retrieve Capital Marker Information | Marker Data |
| US7.4 | Retrieve Decision History | Decision History |
| US7.5 | Generate CSV Report | CSV File |
| US7.6 | Search Reports | Search Results |
| US7.7 | View Report | Report Details |
| US7.8 | Download CSV Report | CSV Download |
| US7.9 | Store Report Snapshot | Report Collection |

### Deliverables

- CSV Report Generator
- Report APIs
- Report Repository

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 9 |
| Unit + API Testing | 3 |
| SIT | 2 |
| **Total** | **14** |

---

# Epic 8 – Security, MongoDB & Application Setup

## Objective

Prepare application infrastructure and security.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US8.1 | Create MongoDB Collections | Collections |
| US8.2 | Create MongoDB Indexes | Indexes |
| US8.3 | Configure Application Credentials | Application Access |
| US8.4 | Configure IT Support Credentials | RO/RW Users |
| US8.5 | Configure Swagger UI | API Documentation |
| US8.6 | Configure Logging | Application Logs |
| US8.7 | Configure Application Properties | Environment Configuration |

### Deliverables

- MongoDB Configuration
- Credentials
- Swagger UI
- Logging

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 5 |
| Unit + API Testing | 2 |
| SIT | 1 |
| **Total** | **8** |

---

# Epic 9 – Audit Trail & Version Management

## Objective

Provide complete operational traceability.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US9.1 | Maintain Audit Trail | Audit Records |
| US9.2 | Maintain Technical Trail | Technical Records |
| US9.3 | Maintain Version History | Version Records |
| US9.4 | Generate Transaction IDs | Transaction Tracking |
| US9.5 | Maintain Report History | Immutable Reports |

### Deliverables

- Audit Framework
- Version Framework
- Transaction Tracking

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 5 |
| Unit + API Testing | 2 |
| SIT | 1 |
| **Total** | **8** |

---

# Epic 10 – Production Readiness

## Objective

Prepare the solution for Production deployment.

### User Stories

| ID | Description | Output |
|----|-------------|--------|
| US10.1 | Package Application | Deployable Artifact |
| US10.2 | Configure Environments | Environment Configuration |
| US10.3 | Prepare Deployment Package | Deployment Scripts |
| US10.4 | Execute Smoke Testing | Smoke Test Results |
| US10.5 | Support Production Deployment | Production Release |

### Deliverables

- Deployment Package
- Production Configuration
- Smoke Testing

### Estimate

| Activity | MD |
|-----------|---:|
| Development | 4 |
| Unit + API Testing | 2 |
| SIT | 1 |
| **Total** | **7** |

---

# Overall Project Estimate

| Activity | Person Days |
|-----------|------------:|
| Development | **79** |
| Unit + API Testing | **29** |
| SIT | **17** |
| **Grand Total** | **125 Person Days** |