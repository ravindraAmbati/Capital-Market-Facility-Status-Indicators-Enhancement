# Capital-Market-Facility-Status-Indicators-Enhancement

## Document of Understanding (DoU)

**Version:** 1.1

---

# Revision History

| Version | Date        | Description                                                                  |
| ------- | ----------- | ---------------------------------------------------------------------------- |
| 1.0     | 15-Jun-2026 | Initial Document of Understanding                                            |
| 1.1     | TBD         | Added Current State, Future State and additional Out of Scope clarifications |

---

# 1. Project Description

The Capital Market Facility Status Indicators Enhancement project aims to enhance the CARM-KSA system by introducing facility-level Capital Market Status Indicators comprising Advised, Committed, and Unconditionally Cancellable.

The enhancement will automate indicator assignment based on approved business logic, support controlled overrides with mandatory justification, introduce review and confirmation controls during recommendation, approval or decline processes, provide Recoverable and Non-Recoverable (R&NR) reporting capabilities, enable administration of business logic tables, and make approved facility status information available for HUB consumption through the existing integration framework (KONG API).

The solution will improve data quality, governance, auditability, and operational transparency while preserving existing approved Credit Applications and current Credit Application workflow processes.

---

# 2. Business Objective

The objectives of this enhancement are:

* Capture and maintain Capital Market Facility Status Indicators at facility level.
* Ensure facility status indicators are derived according to approved business rules.
* Improve governance and review controls within the Credit Application lifecycle.
* Provide reporting visibility for authorized users.
* Make approved facility status information available for HUB consumption.

---

# <span style="color:red">3. Current State (NEW IN V1.1)</span>

<span style="color:red">

## Facility Maintenance

* A single Status Indicator dropdown exists at Facility level.
* Status Indicator values are manually maintained through the existing process.
* Purpose Code continues to be maintained as part of facility information.
* No facility-level override justification capability exists.

## Credit Application Workflow

Current lifecycle:

Create → Recommend ↔ Resubmit → Approve / Decline

* Relationship Managers maintain facility information during Active status.
* Recommenders and Approvers review facility information before actioning applications.
* No mandatory confirmation mechanism exists for facility status review.

## Reporting

* No dedicated R&NR reporting capability exists.
* Facility status indicators are not available through dedicated reporting.

## Administration

* No dedicated administration screen exists for facility status indicator business logic maintenance.

## HUB Integration

* HUB does not consume Capital Market Facility Status Indicators through dedicated APIs.
* Existing integrations remain unchanged.

</span>

---

# <span style="color:red">4. Future State (NEW IN V1.1)</span>

<span style="color:red">

## Facility Maintenance

* Existing Status Indicator functionality will be replaced for applicable Credit Applications by:

  * Advised
  * Committed
  * Unconditionally Cancellable

* Advised and Committed values will be derived from Facility Type logic.

* Unconditionally Cancellable values will be derived from Purpose Code logic.

* Override functionality will be available only during Active status.

* Mandatory justification comments will be required for overrides.

* Existing approved Credit Applications remain unchanged.

## Workflow Controls

* Relationship Managers may update indicators only while the application is Active.
* Recommenders and Approvers cannot modify indicators.
* Recommenders, Approvers and Decliners must confirm review of facility indicators before actioning the application.
* Existing Resubmit functionality remains unchanged.

## Reporting

* Dedicated R&NR reporting capability.
* Read-only Data Grid presentation.
* CSV export functionality.

## Administration

* Facility Type logic maintenance.
* Purpose Code logic maintenance.
* CRUD operations with soft delete.

## HUB Integration

* Approved facility status information exposed through secured APIs.
* HUB retrieves data through KONG.
* API pagination support available.

</span>

---

# 5. Stakeholders and Sign Off

* Credit & Lending Team
* WCA
* CARM IT Team
* HUB Team
* Enterprise Architecture
* Information Security

---

# 6. Scope

## In Scope

### Facility Page Enhancements

* Replace Status Indicator with:

  * Advised
  * Committed
  * Unconditionally Cancellable

* Facility-level override functionality.

* Mandatory comments when override selected.

* Validation rules for Syndicated/Bilateral facilities.

* Validation rules for Non-standard Security Documents.

### Recommend / Approve / Decline Page Enhancements

* Mandatory confirmation checkbox.
* Report review capability.
* Existing Resubmit functionality retained.

### Reporting

* Read-only Data Grid.
* CSV download.
* Facility-level status reporting.
* Confirmation reporting.

### Logic Table Administration

* Facility Type logic maintenance.
* Purpose Code logic maintenance.
* CRUD operations with soft delete.

### HUB Integration

* API-based access to approved facility status information.
* KONG-based integration architecture.

---

# 7. Assumptions and Dependencies

## Assumptions

* Credit & Lending Team and WCA own the business rules.
* Existing approved applications remain unchanged.
* Existing workflow actions remain unchanged.
* Existing Resubmit functionality remains unchanged.

## Dependencies

* Facility Type reference data.
* Purpose Code reference data.
* Existing Credit Application workflow.
* Existing facility maintenance functionality.
* Existing recommendation, approval and decline processes.
* HUB consumption requirements.

---

# 8. Acceptance Criteria

The solution shall be considered accepted when:

* Facility indicators are automatically populated according to business logic.
* Override controls function correctly.
* Mandatory comments are enforced.
* Recommendation, Approval and Decline confirmation controls operate correctly.
* Reporting is available in Data Grid and CSV formats.
* Logic table maintenance operates correctly.
* Approved facility status information is available through APIs.
* Existing approved applications remain unaffected.

---

# 9. Business Rules

1. Advised and Committed values are derived from Facility Type.
2. Unconditionally Cancellable value is derived from Purpose Code.
3. Override is permitted only during Active status.
4. Override justification is mandatory.
5. Recommendation, Approval and Decline require confirmation.
6. Indicators cannot be modified after recommendation.
7. Existing approved applications remain unchanged.
8. Validation rules apply to Syndicated/Bilateral facilities.
9. Validation rules apply to Non-standard Security Documents.
10. Cancellation Credit Applications with proposed amount equal to zero are excluded.
11. Logic table cache refresh occurs every 2 hours.

---

# 10. Out of Scope

* Changes to existing Facility Creation functionality.
* Changes to Credit Application workflow.
* Changes to existing approved Credit Applications.
* Cancellation Credit Applications where Facility Proposed Amount equals zero.

## <span style="color:red">Additional Out of Scope Clarifications (NEW IN V1.1)</span>

<span style="color:red">

* No changes to Limit Creation functionality in HUB.
* No changes to Facility Creation functionality in CARM.
* Existing operational processes remain AS-IS.
* No CARM push integration to HUB.
* Newly introduced status indicators will be exposed through APIs only.
* No automatic update of status indicators in HUB.
* HUB remains responsible for consuming data from CARM APIs.
* Existing enterprise integration architecture remains unchanged.

</span>

---

# 11. References and Attachments

* Business Logic Table (Facility Type)
* Business Logic Table (Purpose Code)
* API Specifications (to be detailed in FDD/TDD)
* Reporting Layout Specifications
* Administration Screen Specifications

---
