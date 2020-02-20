package io.datappeal.spring.off.shutdown;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ConfigurableApplicationContext;

class SpringShutdownerTest {

    @Test
    void testShutdownSpringContext() {
        final ConfigurableApplicationContext mock = Mockito.mock(ConfigurableApplicationContext.class);
        final SpringShutdowner shutdowner = SpringShutdowner.of(mock);
        shutdowner.shutdown();

        Mockito.verify(mock).close();
    }
}
