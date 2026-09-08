package com.sab.fcm.carm.retry;

import java.util.concurrent.Callable;

public class RetryExecutor {

    private final int maxAttempts;
    private final long initialBackoffMs;
    private final long maxBackoffMs;
    private final double multiplier;

    public RetryExecutor(
            int maxAttempts,
            long initialBackoffMs,
            long maxBackoffMs,
            double multiplier) {

        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1");
        }
        if (initialBackoffMs < 0) {
            throw new IllegalArgumentException("initialBackoffMs must be >= 0");
        }
        if (maxBackoffMs < initialBackoffMs) {
            throw new IllegalArgumentException(
                    "maxBackoffMs must be >= initialBackoffMs");
        }
        if (multiplier < 1.0) {
            throw new IllegalArgumentException("multiplier must be >= 1.0");
        }

        this.maxAttempts = maxAttempts;
        this.initialBackoffMs = initialBackoffMs;
        this.maxBackoffMs = maxBackoffMs;
        this.multiplier = multiplier;
    }

    public <T> T execute(Callable<T> operation) throws Exception {
        Exception lastException = null;
        long backoff = initialBackoffMs;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.call();
            } catch (Exception e) {
                lastException = e;

                if (attempt == maxAttempts) {
                    break;
                }

                if (backoff > 0) {
                    Thread.sleep(backoff);
                }

                long nextBackoff = (long) Math.ceil(backoff * multiplier);
                backoff = Math.min(nextBackoff, maxBackoffMs);
            }
        }

        throw new RetryExhaustedException(
                "Operation failed after " + maxAttempts + " attempts",
                lastException);
    }
}
