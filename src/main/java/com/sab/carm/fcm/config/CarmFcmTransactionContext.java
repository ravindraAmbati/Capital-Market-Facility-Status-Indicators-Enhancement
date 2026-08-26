package com.sab.carm.fcm.config;

public final class CarmFcmTransactionContext {

    private static final ThreadLocal<Context> CONTEXT =
            new ThreadLocal<>();

    private CarmFcmTransactionContext() {
    }

    public static void initialize(
            String correlationId,
            String transactionId) {

        CONTEXT.set(
                new Context(correlationId, transactionId));
    }

    public static String getCorrelationId() {
        Context context = CONTEXT.get();
        return context == null
                ? null
                : context.correlationId;
    }

    public static String getTransactionId() {
        Context context = CONTEXT.get();
        return context == null
                ? null
                : context.transactionId;
    }

    public static void clear() {
        CONTEXT.remove();
    }

    private static final class Context {

        private final String correlationId;
        private final String transactionId;

        private Context(
                String correlationId,
                String transactionId) {

            this.correlationId = correlationId;
            this.transactionId = transactionId;
        }
    }
}
