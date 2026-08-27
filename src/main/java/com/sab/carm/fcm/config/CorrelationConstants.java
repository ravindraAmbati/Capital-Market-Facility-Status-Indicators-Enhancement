package com.sab.carm.fcm.config;

public final class CorrelationConstants {

    public static final String CARM_FCM_CORRELATION_ID =
            "X-CARM-FCM-CorrelationId";

    public static final String CARM_FCM_TRANSACTION_ID =
            "X-CARM-FCM-TransactionId";

    public static final String MDC_CORRELATION_ID =
            "correlationId";

    private CorrelationConstants() {
    }
}