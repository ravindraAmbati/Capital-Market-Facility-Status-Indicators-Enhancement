package com.sab.fcm.carm.retry;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class RetryExecutorTest {

    @Test
    public void shouldRetryAndEventuallySucceed() throws Exception {

        final AtomicInteger attempts = new AtomicInteger();

        RetryExecutor executor =
                new RetryExecutor(3, 1, 5, 2.0);

        String result =
                executor.execute(new Callable<String>() {
                    @Override
                    public String call() {
                        if (attempts.incrementAndGet() < 3) {
                            throw new RuntimeException("temporary failure");
                        }
                        return "SUCCESS";
                    }
                });

        assertEquals("SUCCESS", result);
        assertEquals(3, attempts.get());
    }

    @Test(expected = RetryExhaustedException.class)
    public void shouldFailAfterMaximumAttempts() throws Exception {

        final AtomicInteger attempts = new AtomicInteger();

        RetryExecutor executor =
                new RetryExecutor(3, 1, 5, 2.0);

        executor.execute(new Callable<String>() {
            @Override
            public String call() {
                attempts.incrementAndGet();
                throw new RuntimeException("failure");
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidAttempts() {
        new RetryExecutor(0, 1, 5, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidBackoff() {
        new RetryExecutor(3, 10, 5, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectMultiplierBelowOne() {
        new RetryExecutor(3, 1, 5, 0.5);
    }
}
