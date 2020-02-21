package io.datappeal.spring.off.configuration;

public class FixedShutdownConfiguration implements ShutdownConfiguration {

    public static final int BASE_DELAY = 10_000;
    public static final int MAX_DEADLINE = 30_000;
    private final long baseDelay;
    private final long maxDeadline;
    public FixedShutdownConfiguration(long baseDelay, long maxDeadline) {
        if (baseDelay > maxDeadline) {
            throw new IllegalArgumentException("base delay can't be greater than max deadline");
        }
        this.baseDelay = baseDelay;
        this.maxDeadline = maxDeadline - baseDelay;
    }

    public FixedShutdownConfiguration() {
        this(BASE_DELAY, MAX_DEADLINE);
    }

    public static FixedShutdownConfiguration immediate() {
        return new FixedShutdownConfiguration(0L, 0L);
    }

    @Override
    public long shutdownBaseDelay() {
        return this.baseDelay;
    }

    @Override
    public long shutdownMaxDeadline() {
        return this.maxDeadline;
    }
}
