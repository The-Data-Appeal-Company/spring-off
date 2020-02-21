package io.datappeal.spring.off.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FixedShutdownConfigurationTest {

    @Test
    void testInstanceImmediate() {
        final FixedShutdownConfiguration immediate = FixedShutdownConfiguration.immediate();
        assertThat(immediate.shutdownBaseDelay())
                .isZero();
        assertThat(immediate.shutdownMaxDeadline())
                .isZero();
    }

    @Test
    void testInstanceCustomParam() {
        final FixedShutdownConfiguration custom = new FixedShutdownConfiguration(10L, 20L);

        assertThat(custom.shutdownBaseDelay())
                .isEqualTo(10L);

        assertThat(custom.shutdownMaxDeadline())
                .isEqualTo(20L - custom.shutdownBaseDelay());
    }

    @Test
    void testInstanceWrongBaseDelay() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new FixedShutdownConfiguration(30, 20)
        );
    }

}
