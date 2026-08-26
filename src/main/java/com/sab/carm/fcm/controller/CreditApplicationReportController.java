package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersReportRow;
import com.sab.carm.fcm.service.CreditApplicationReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carm/fcm/report")
public class CreditApplicationReportController {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    private final CreditApplicationReportService service;

    public CreditApplicationReportController(
            CreditApplicationReportService service) {
        this.service = service;
    }

    @GetMapping(produces = "text/csv")
    public ResponseEntity<String> getReport(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @RequestParam String relationshipId,
            @RequestParam String serialNo) {

        CreditApplicationReportResponse report =
                service.getReport(relationshipId, serialNo);

        String csv = toCsv(report);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + relationshipId
                                + "_"
                                + serialNo
                                + "_facility-capital-markers.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    private String toCsv(CreditApplicationReportResponse report) {

        StringBuilder csv = new StringBuilder();

        csv.append(
                "RELATIONSHIP_ID,SERIAL_NO,FACILITY_NO,"
                        + "CUSTOMER_ID,BORROWING_GROUP,PROPOSAL_TYPE,"
                        + "APPLICATION_STATUS,FACILITY_TYPE,CARM_PURPOSE_CODE,"
                        + "ADVISED,ADVISED_OVERRIDE,ADVISED_OVERRIDE_JUSTIFICATION,"
                        + "COMMITTED,COMMITTED_OVERRIDE,"
                        + "COMMITTED_OVERRIDE_JUSTIFICATION,"
                        + "UNCONDITIONAL_CANCELLABLE,"
                        + "UNCONDITIONAL_CANCELLABLE_OVERRIDE,"
                        + "UNCONDITIONAL_CANCELLABLE_OVERRIDE_JUSTIFICATION,"
                        + "STANDING_SECURITY_DOCUMENT,SENIORITY_TYPE,"
                        + "CONSENT_DECISION,CONSENT_HUB_USER_ID,"
                        + "CONSENTED_AT,CONSENT_CORRELATION_ID,"
                        + "CONSENT_TRANSACTION_ID")
                .append('\n');

        List<CreditApplicationConsent.Consent> consents =
                report.getConsents();

        for (FacilityCapitalMarkersReportRow facility :
                report.getFacilities()) {

            if (consents.isEmpty()) {
                appendFacility(csv, facility, null);
            } else {
                for (CreditApplicationConsent.Consent consent :
                        consents) {
                    appendFacility(csv, facility, consent);
                }
            }
        }

        if (report.getFacilities().isEmpty()
                && !consents.isEmpty()) {
            for (CreditApplicationConsent.Consent consent :
                    consents) {
                appendFacility(csv, null, consent);
            }
        }

        return csv.toString();
    }

    private void appendFacility(
            StringBuilder csv,
            FacilityCapitalMarkersReportRow f,
            CreditApplicationConsent.Consent consent) {

        String[] values = new String[] {
                f == null ? null : f.getRelationshipId(),
                f == null ? null : f.getSerialNo(),
                f == null ? null : f.getFacilityNo(),
                f == null ? null : f.getCustomerId(),
                f == null ? null : f.getBorrowingGroup(),
                f == null ? null : f.getProposalType(),
                f == null ? null : f.getApplicationStatus(),
                f == null ? null : f.getFacilityType(),
                f == null ? null : f.getCarmPurposeCode(),
                f == null ? null : f.getAdvised(),
                f == null ? null : String.valueOf(f.isAdvisedOverride()),
                f == null ? null : f.getAdvisedOverrideJustification(),
                f == null ? null : f.getCommitted(),
                f == null ? null : String.valueOf(f.isCommittedOverride()),
                f == null ? null : f.getCommittedOverrideJustification(),
                f == null ? null : f.getUnconditionalCancellable(),
                f == null ? null :
                        String.valueOf(
                                f.isUnconditionalCancellableOverride()),
                f == null ? null :
                        f.getUnconditionalCancellableOverrideJustification(),
                f == null ? null : f.getStandingSecurityDocument(),
                f == null ? null : f.getSeniorityType(),
                consent == null ? null : consent.getDecision(),
                consent == null ? null : consent.getHubUserId(),
                consent == null || consent.getConsentedAt() == null
                        ? null : consent.getConsentedAt().toString(),
                consent == null ? null : consent.getCorrelationId(),
                consent == null ? null : consent.getTransactionId()
        };

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                csv.append(',');
            }
            csv.append(csvValue(values[i]));
        }
        csv.append('\n');
    }

    private String csvValue(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
