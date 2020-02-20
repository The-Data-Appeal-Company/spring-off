package io.datappeal.spring.off.filter;

public interface InFlightCounter {
    void incr();

    void decr();

    long inFlight();
}
