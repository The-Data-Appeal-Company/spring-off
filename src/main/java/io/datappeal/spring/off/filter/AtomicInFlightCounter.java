package io.datappeal.spring.off.filter;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicInFlightCounter extends Observable implements InFlightCounter {

    private final AtomicLong counter;

    public AtomicInFlightCounter() {
        this.counter = new AtomicLong(0);
    }

    @Override
    public void incr() {
        this.counter.incrementAndGet();
    }

    @Override
    public void decr() {
        this.counter.decrementAndGet();
    }

    @Override
    public long inFlight() {
        return this.counter.get();
    }

}
