package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.CarmFcmTransactionContext;
import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.service.CreditApplicationReportCsvWriter;
import com.sab.carm.fcm.service.CreditApplicationReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carm/fcm/report")
public class CreditApplicationReportController {

    public static final String CORRELATION_ID_HEADER =
            "X-CARM-FCM-CorrelationId";

    public static final String TRANSACTION_ID_HEADER =
            "X-CARM-FCM-TransactionId";

    private final CreditApplicationReportService service;
    private final CreditApplicationReportCsvWriter csvWriter;

    public CreditApplicationReportController(
            CreditApplicationReportService service,
            CreditApplicationReportCsvWriter csvWriter) {
        this.service = service;
        this.csvWriter = csvWriter;
    }

    @GetMapping(produces = "text/csv")
    public ResponseEntity<String> getReport(
            @RequestHeader(CORRELATION_ID_HEADER) String correlationId,
            @RequestParam String relationshipId,
            @RequestParam String serialNo) {

        CreditApplicationReportResponse report =
                service.getReport(
                        relationshipId,
                        serialNo);

        String csv = csvWriter.write(report);

        ResponseEntity.BodyBuilder builder =
                ResponseEntity.ok()
                        .header(
                                CORRELATION_ID_HEADER,
                                correlationId)
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\""
                                        + relationshipId
                                        + "_"
                                        + serialNo
                                        + "_facility-capital-markers.csv\"")
                        .contentType(
                                MediaType.parseMediaType(
                                        "text/csv"));

        String transactionId =
                CarmFcmTransactionContext
                        .getTransactionId();

        if (transactionId != null
                && !transactionId.trim().isEmpty()) {
            builder.header(
                    TRANSACTION_ID_HEADER,
                    transactionId);
        }

        return builder.body(csv);
    }
}
