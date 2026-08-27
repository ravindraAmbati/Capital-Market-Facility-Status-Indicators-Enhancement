package com.sab.carm.fcm.controller;

import com.sab.carm.fcm.config.CarmFcmTransactionContext;
import com.sab.carm.fcm.config.IntegrationResponseHeaderFactory;
import com.sab.carm.fcm.dto.integration.CreditApplicationReportResponse;
import com.sab.carm.fcm.service.CreditApplicationReportCsvWriter;
import com.sab.carm.fcm.service.CreditApplicationReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carm/fcm/report")
public class CreditApplicationReportController {

    public static final String CORRELATION_ID_HEADER =
            IntegrationResponseHeaderFactory.CORRELATION_ID_HEADER;

    public static final String TRANSACTION_ID_HEADER =
            IntegrationResponseHeaderFactory.TRANSACTION_ID_HEADER;

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
                service.getReport(relationshipId, serialNo);

        String csv = csvWriter.write(report);

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .headers(IntegrationResponseHeaderFactory
                        .httpHeaders(correlationId))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + relationshipId
                                + "_" + serialNo
                                + "_facility-capital-markers.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"));

        String transactionId =
                CarmFcmTransactionContext.getTransactionId();

        if (transactionId != null
                && !transactionId.trim().isEmpty()) {
            builder.header(TRANSACTION_ID_HEADER, transactionId);
        }

        return builder.body(csv);
    }
}
