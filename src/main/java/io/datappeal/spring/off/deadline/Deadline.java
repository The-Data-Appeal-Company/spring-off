package io.datappeal.spring.off.deadline;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Deadline {

    private final long duration;
    private long begin;
    public Deadline(long duration) {
        this.duration = duration;
    }

    public static Deadline of(final long duration, final TimeUnit unit) {
        return new Deadline(unit.toMillis(duration));
    }

    public Deadline fromNow() {
        this.begin = now();
        return this;
    }

    public long timeLeft() {
        return this.duration - (now() - this.begin);
    }

    public boolean isExpired() {
        return timeLeft() <= 0;
    }

    private long now() {
        return Instant.now().toEpochMilli();
    }

}
