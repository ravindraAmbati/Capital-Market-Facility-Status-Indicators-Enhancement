# Capital Market Facility Status Indicators Enhancement

## Project Description

The Capital Market Facility Status Indicators Enhancement project aims to enhance the Credit Application Management (CARM) system by introducing facility-level Capital Market Status Indicators comprising **Advised**, **Committed**, and **Unconditionally Cancellable**.

The enhancement will automate indicator assignment based on approved business logic, support controlled overrides with mandatory justification, introduce review and confirmation controls during recommendation, approval, and decline processes, provide Recoverable and Non-Recoverable (R&NR) reporting capabilities, enable administration of business logic tables, and make approved facility status information available for HUB consumption through the existing integration framework.

The solution will improve data quality, governance, auditability, and operational transparency while preserving existing approved Credit Applications and current Credit Application workflow processes.

---

# Business Objective

The objectives of this enhancement are to:

* Capture and maintain Capital Market Facility Status Indicators at facility level.
* Ensure facility classification aligns with underlying facility documentation and business requirements.
* Improve governance and review controls during recommendation, approval, and decline activities.
* Provide reporting visibility for Credit & Lending, WCA, and other authorized users.
* Make approved facility status information available for HUB consumption.

---

# Scope

## In Scope

### 1. Facility Page Enhancements

* Replace existing Status Indicator with:

  * Advised
  * Committed
  * Unconditionally Cancellable
* Automated population of indicators.
* Facility-level override functionality.
* Facility-level justification comments.
* Validation rules.
* Syndicated/Bilateral validations.
* Non-standard Security Documents validations.

### 2. Recommend / Approve / Decline Enhancements

* Mandatory review confirmation.
* Mandatory confirmation before:

  * Recommend
  * Approve
  * Decline
* Report review guidance.
* Existing Resubmit process remains the correction mechanism.

### 3. R&NR Reporting

* Read-only reporting capability.
* Data Grid presentation.
* CSV export.
* Search and filtering capability.
* Facility status reporting.
* Validation exception reporting.
* Confirmation audit reporting.

### 4. Logic Table Administration

* Facility Type logic maintenance.
* Purpose Code logic maintenance.
* Create functionality.
* Read functionality.
* Update functionality.
* Soft Delete functionality.

### 5. HUB Integration

* Exposure of approved facility status information.
* Support for HUB consumption.
* Integration through existing enterprise integration architecture.

---

## Out of Scope

* Changes to existing Purpose Code functionality.
* Changes to existing Credit Application workflow.
* Changes to existing Resubmit functionality.
* Changes to existing approved Credit Applications.
* Changes to existing Function Access Code framework.
* Cancellation Credit Applications where Facility Proposed Amount equals zero.

---

# Current State

Currently:

* Facility status is maintained through a single **Status Indicator** field.
* Purpose Code functionality already exists.
* Credit Applications follow the existing workflow:

  * Create
  * Recommend
  * Resubmit
  * Approve
  * Decline

---

# Future State

The solution will introduce the following facility-level indicators:

* Advised
* Committed
* Unconditionally Cancellable

These indicators will support:

* Automated population.
* Controlled override capability.
* Mandatory justification.
* Validation checks.
* Reporting.
* Review confirmation.
* HUB integration.

---

# Facility Page Requirements

## Status Indicator Replacement

The existing Status Indicator shall be replaced by:

* Advised
* Committed
* Unconditionally Cancellable

for applicable Credit Applications.

## Existing Approved Applications

* Existing approved Credit Applications remain unchanged.
* Existing Status Indicator remains available for historical approved applications.

## Automated Population

### Advised and Committed

Automatically populated based on:

* Facility Type

using maintained business logic.

### Unconditionally Cancellable

Automatically populated based on:

* Purpose Code

using maintained business logic.

## Override Functionality

### Advised & Committed

* Common override checkbox.
* Common justification comment.

### Unconditionally Cancellable

* Separate override checkbox.
* Separate justification comment.

## Override Rules

* Allowed only when Credit Application status is **Active**.
* Comment mandatory when override checkbox is selected.
* Maximum comment length: **250 characters**.
* Status indicators become editable only when override is selected.
* Status indicators revert to system-generated values when override is removed.

## Validation Rules

Validation shall be supported for:

* Syndicated facilities.
* Bilateral facilities.
* Non-standard Security Documents.
* Facility indicator consistency.

---

# Recommend / Approve / Decline Requirements

## Review Confirmation

Users performing Recommend, Approve, or Decline actions must confirm:

> I confirm that I have reviewed all facilities and verified that the Capital Market Facility Status Indicators are correct.

## Mandatory Confirmation

Confirmation is mandatory before action submission.

## Facility Review

Reviewers may not modify facility indicators during:

* Recommend
* Approve
* Decline

## Resubmission

Where issues are identified, the existing Resubmit functionality shall be used.

---

# R&NR Reporting Requirements

## Navigation

```text
Others → R&NR Report
```

## Report Type

* Read-only
* Searchable
* Filterable

## Supported Formats

* Data Grid
* CSV Export

## Report Header

* Relationship ID
* Serial No
* Generated Date
* Credit Application Status

## Report Detail

* Facility Number
* Facility Type
* Advised
* Committed
* Override Status
* Override Comments
* Purpose Code
* Unconditionally Cancellable
* Override Status
* Override Comments
* Validation Errors / Exceptions
* Last Updated By
* Last Updated Date and Time

## Confirmation Information

* User
* Action
* Confirmation Status
* Date and Time

---

# Logic Table Administration

## Facility Type Logic

Used to derive:

* Advised
* Committed

## Purpose Code Logic

Used to derive:

* Unconditionally Cancellable

## Administration Functions

Supported operations:

* Create
* Read
* Update
* Soft Delete

## Deletion Rules

* Hard Delete is not supported.
* Soft Delete only.

---

# HUB Integration

## Integration Objective

Provide approved facility status information for HUB consumption.

## Data Availability

Approved Credit Application facility status information shall be available for extraction.

## Application Status

Application Status shall be included in the information made available for consumption.

## Pagination

Integration services shall support pagination.

---

# Business Rules

| ID     | Rule                                                                                       |
| ------ | ------------------------------------------------------------------------------------------ |
| BRR-01 | Advised and Committed values are derived from Facility Type.                               |
| BRR-02 | Unconditionally Cancellable value is derived from Purpose Code.                            |
| BRR-03 | Override is permitted only when Credit Application status is Active.                       |
| BRR-04 | Override justification is mandatory when override is selected.                             |
| BRR-05 | Comment length shall not exceed 250 characters.                                            |
| BRR-06 | Recommend, Approve, and Decline require review confirmation.                               |
| BRR-07 | Facility indicators cannot be modified during Recommend, Approve, or Decline activities.   |
| BRR-08 | Existing approved Credit Applications remain unchanged.                                    |
| BRR-09 | Validation rules apply to Syndicated/Bilateral facilities.                                 |
| BRR-10 | Validation rules apply to Non-standard Security Documents.                                 |
| BRR-11 | Cancellation Credit Applications with Facility Proposed Amount equal to zero are excluded. |

---

# Assumptions

* Credit & Lending Team and WCA own the business logic and validation rules.
* Existing approved Credit Applications remain unchanged.
* Existing Status Indicator values remain available for historical approved applications.
* Users who can access facilities can access the R&NR report.
* Existing workflow actions remain unchanged.
* Existing Resubmit functionality remains unchanged.

---

# Dependencies

* Facility Type reference data.
* Purpose Code reference data.
* Existing Credit Application workflow.
* Existing facility maintenance functionality.
* Existing recommendation, approval, and decline processes.
* HUB consumption requirements.

---

# Acceptance Criteria

The solution shall be considered accepted when:

1. Facility indicators are automatically populated according to approved business logic.
2. Override controls operate according to agreed business rules.
3. Mandatory justification requirements are enforced.
4. Recommend, Approve, and Decline confirmation controls are enforced.
5. Validation rules operate as defined.
6. R&NR report is available in Data Grid and CSV formats.
7. Logic table administration functions operate correctly.
8. Approved facility status information is available for HUB consumption.
9. Existing approved Credit Applications remain unaffected.
10. Cancellation Credit Applications with Facility Proposed Amount equal to zero remain unaffected.

---

# Stakeholders

* Business
* Credit & Lending
* WCA
* Technology
* HUB Integration Team
