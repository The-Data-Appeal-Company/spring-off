package io.datappeal.spring.off.configuration;

public interface ShutdownConfiguration {
    long shutdownBaseDelay();
    long shutdownMaxDeadline();
}
