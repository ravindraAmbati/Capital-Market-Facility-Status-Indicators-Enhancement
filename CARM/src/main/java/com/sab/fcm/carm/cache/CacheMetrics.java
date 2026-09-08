package com.sab.fcm.carm.cache;

import java.util.concurrent.atomic.AtomicLong;

public class CacheMetrics {

    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();

    public void hit() {
        hits.incrementAndGet();
    }

    public void miss() {
        misses.incrementAndGet();
    }

    public long getHits() {
        return hits.get();
    }

    public long getMisses() {
        return misses.get();
    }

    public long getTotal() {
        return getHits() + getMisses();
    }

    public double getHitRatio() {
        long total = getTotal();
        return total == 0 ? 0.0 : (double) getHits() / total;
    }

    public double getMissRatio() {
        long total = getTotal();
        return total == 0 ? 0.0 : (double) getMisses() / total;
    }
}
