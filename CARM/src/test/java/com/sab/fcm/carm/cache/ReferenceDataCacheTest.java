package com.sab.fcm.carm.cache;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sab.fcm.carm.notification.FailureNotificationService;
import com.sab.fcm.carm.retry.RetryExecutor;
import com.sab.fcm.carm.service.ReferenceDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataCacheTest {

    @Mock
    private ReferenceDataProvider provider;

    @Mock
    private FailureNotificationService notificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RetryExecutor retryExecutor;
    private ObjectNode data;

    @Before
    public void setUp() {
        retryExecutor = new RetryExecutor(1, 0, 0, 2.0);
        data = objectMapper.createObjectNode();
        data.put("facilityTypes", "facility-data");
    }

    @Test
    public void shouldLoadOnFirstRequestAndRecordMiss() {
        when(provider.loadReferenceData()).thenReturn(data);

        ReferenceDataCache cache = newCache(10000, 30000);

        JsonNode result = cache.get();

        assertEquals("facility-data",
                result.get("facilityTypes").asText());
        assertEquals(1, cache.getMetrics().getMisses());
        assertEquals(0, cache.getMetrics().getHits());
        verify(provider, times(1)).loadReferenceData();
    }

    @Test
    public void shouldReturnCachedDataOnSecondRequest() {
        when(provider.loadReferenceData()).thenReturn(data);

        ReferenceDataCache cache = newCache(10000, 30000);

        cache.get();
        cache.get();

        verify(provider, times(1)).loadReferenceData();
        assertEquals(1, cache.getMetrics().getMisses());
        assertEquals(1, cache.getMetrics().getHits());
    }

    @Test
    public void shouldProtectAgainstCacheStampede() throws Exception {
        final AtomicInteger calls = new AtomicInteger();
        final CountDownLatch release = new CountDownLatch(1);

        when(provider.loadReferenceData()).thenAnswer(
                invocation -> {
                    calls.incrementAndGet();
                    release.await();
                    return data;
                });

        final ReferenceDataCache cache = newCache(10000, 30000);
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<JsonNode>> futures = new ArrayList<Future<JsonNode>>();

        for (int i = 0; i < 20; i++) {
            futures.add(executor.submit(new Callable<JsonNode>() {
                @Override
                public JsonNode call() {
                    return cache.get();
                }
            }));
        }

        Thread.sleep(100);
        assertEquals(1, calls.get());

        release.countDown();

        for (Future<JsonNode> future : futures) {
            assertNotNull(future.get());
        }

        executor.shutdownNow();

        assertEquals(1, calls.get());
    }

    @Test
    public void shouldReturnStaleDataAndTriggerAsyncRefreshAfterSoftTtl()
            throws Exception {

        ObjectNode first = objectMapper.createObjectNode();
        first.put("version", "1");

        ObjectNode second = objectMapper.createObjectNode();
        second.put("version", "2");

        when(provider.loadReferenceData())
                .thenReturn(first)
                .thenReturn(second);

        Executor directExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        ReferenceDataCache cache =
                new ReferenceDataCache(
                        provider,
                        retryExecutor,
                        notificationService,
                        directExecutor,
                        20,
                        1000);

        assertEquals("1", cache.get().get("version").asText());

        Thread.sleep(40);

        assertEquals("1", cache.get().get("version").asText());

        assertEquals("2",
                cache.getCachedValueForTest()
                        .get("version").asText());

        verify(provider, times(2)).loadReferenceData();
    }

    @Test
    public void shouldInvalidateCache() {
        when(provider.loadReferenceData()).thenReturn(data);

        ReferenceDataCache cache = newCache(10000, 30000);

        cache.get();
        assertTrue(cache.isLoaded());

        cache.invalidate();

        assertFalse(cache.isLoaded());
    }

    @Test
    public void shouldKeepLastKnownGoodDataWhenHardRefreshFails()
            throws Exception {

        when(provider.loadReferenceData())
                .thenReturn(data)
                .thenThrow(new RuntimeException("FCM unavailable"));

        Executor directExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };

        ReferenceDataCache cache =
                new ReferenceDataCache(
                        provider,
                        retryExecutor,
                        notificationService,
                        directExecutor,
                        20,
                        30);

        cache.get();
        Thread.sleep(50);

        JsonNode result = cache.get();

        assertEquals(
                "facility-data",
                result.get("facilityTypes").asText());

        verify(notificationService, atLeastOnce())
                .notifyFailure(anyString(), any(Exception.class));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailInitialLoadWhenFcmUnavailable() {
        when(provider.loadReferenceData())
                .thenThrow(new RuntimeException("FCM unavailable"));

        ReferenceDataCache cache = newCache(10000, 30000);
        cache.get();
    }

    private ReferenceDataCache newCache(
            long softTtl,
            long hardTtl) {

        return new ReferenceDataCache(
                provider,
                retryExecutor,
                notificationService,
                new Executor() {
                    @Override
                    public void execute(Runnable command) {
                        command.run();
                    }
                },
                softTtl,
                hardTtl);
    }
}
