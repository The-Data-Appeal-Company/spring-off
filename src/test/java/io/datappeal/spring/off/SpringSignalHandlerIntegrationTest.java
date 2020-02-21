package io.datappeal.spring.off;

import io.datappeal.spring.off.app.GracefulShutdownApp;
import io.datappeal.spring.off.common.TestableSignalListener;
import io.datappeal.spring.off.filter.InFlightCounter;
import io.datappeal.spring.off.shutdown.Shutdowner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@SpringBootTest(
        classes = {
                SpringSignalHandlerIntegrationTest.TestSpringApplication.class,
                SpringSignalHandlerIntegrationTest.TestBeans.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SpringSignalHandlerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestableSignalListener testableSignalListener;

    @MockBean
    private Shutdowner shutdowner;

    @MockBean
    private InFlightCounter inFlightCounter;

    @Test
    public void shouldShutDownWhenTriggered() {
        this.testableSignalListener.trigger();
        Mockito.verify(shutdowner).shutdown();

    }

    @GracefulShutdown
    @SpringBootApplication
    public static class TestSpringApplication {
        public static void main(String[] args) {
            GracefulShutdownApp.run(TestSpringApplication.class, args);
        }
    }

    @Configuration
    public static class TestBeans {
        @Bean
        @Primary
        public TestableSignalListener signalListener() {
            return new TestableSignalListener();
        }

    }
}
