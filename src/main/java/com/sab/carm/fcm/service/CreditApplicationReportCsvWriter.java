package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersReportRow;
import com.sab.carm.fcm.entity.CreditApplicationConsent;
import org.springframework.stereotype.Component;

@Component
public class CreditApplicationReportCsvWriter {

    private static final String HEADER =
            "RECORD_TYPE,RELATIONSHIP_ID,SERIAL_NO,FACILITY_NO,"
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
                    + "CONSENT_TRANSACTION_ID";

    public String write(CreditApplicationReportResponse report) {

        StringBuilder csv = new StringBuilder();
        csv.append(HEADER).append('\n');

        for (FacilityCapitalMarkersReportRow facility :
                report.getFacilities()) {
            appendFacility(csv, facility);
        }

        for (CreditApplicationConsent.Consent consent :
                report.getConsents()) {
            appendConsent(csv, report, consent);
        }

        return csv.toString();
    }

    private void appendFacility(
            StringBuilder csv,
            FacilityCapitalMarkersReportRow f) {

        appendRow(csv,
                "FACILITY",
                f.getRelationshipId(),
                f.getSerialNo(),
                f.getFacilityNo(),
                f.getCustomerId(),
                f.getBorrowingGroup(),
                f.getProposalType(),
                f.getApplicationStatus(),
                f.getFacilityType(),
                f.getCarmPurposeCode(),
                f.getAdvised(),
                String.valueOf(f.isAdvisedOverride()),
                f.getAdvisedOverrideJustification(),
                f.getCommitted(),
                String.valueOf(f.isCommittedOverride()),
                f.getCommittedOverrideJustification(),
                f.getUnconditionalCancellable(),
                String.valueOf(
                        f.isUnconditionalCancellableOverride()),
                f.getUnconditionalCancellableOverrideJustification(),
                f.getStandingSecurityDocument(),
                f.getSeniorityType(),
                null,
                null,
                null,
                null,
                null);
    }

    private void appendConsent(
            StringBuilder csv,
            CreditApplicationReportResponse report,
            CreditApplicationConsent.Consent consent) {

        appendRow(csv,
                "CONSENT",
                report.getRelationshipId(),
                report.getSerialNo(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                consent.getDecision(),
                consent.getHubUserId(),
                consent.getConsentedAt() == null
                        ? null
                        : consent.getConsentedAt().toString(),
                consent.getCorrelationId(),
                consent.getTransactionId());
    }

    private void appendRow(
            StringBuilder csv,
            String... values) {

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

        return "\""
                + value.replace("\"", "\"\"")
                + "\"";
    }
}
