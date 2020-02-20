package io.datappeal.spring.off.filter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicInFlightCounterTest {

    @Test
    void testIncr() {
        final AtomicInFlightCounter atomicInFlightCounter = new AtomicInFlightCounter();
        atomicInFlightCounter.incr();
        atomicInFlightCounter.incr();
        atomicInFlightCounter.incr();

        assertThat(atomicInFlightCounter.inFlight())
                .isEqualTo(3);
    }

    @Test
    void testDecr() {
        final AtomicInFlightCounter atomicInFlightCounter = new AtomicInFlightCounter();
        atomicInFlightCounter.decr();
        atomicInFlightCounter.decr();
        atomicInFlightCounter.decr();

        assertThat(atomicInFlightCounter.inFlight())
                .isEqualTo(-3);
    }

    @Test
    void testInFlight() {
        final AtomicInFlightCounter atomicInFlightCounter = new AtomicInFlightCounter();
        assertThat(atomicInFlightCounter.inFlight())
                .isZero();
    }
}
