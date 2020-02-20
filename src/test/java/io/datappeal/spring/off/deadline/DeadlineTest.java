package io.datappeal.spring.off.deadline;

import io.datappeal.spring.off.common.Sleep;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeadlineTest {

    @Test
    public void shouldSetExpiredAfterDuration() {
        final Deadline deadline = Deadline.of(100, TimeUnit.MILLISECONDS);
        deadline.fromNow();
        assertThat(deadline.timeLeft())
                .isGreaterThan(0L);
        assertFalse(deadline.isExpired());

        Sleep.sleep(110);

        assertTrue(deadline.isExpired());
        assertThat(deadline.timeLeft())
                .isLessThanOrEqualTo(0L);
    }


}
