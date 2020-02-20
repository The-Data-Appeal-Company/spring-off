package io.datappeal.spring.off;

import io.datappeal.spring.off.common.TestableSignalListener;
import io.datappeal.spring.off.filter.InFlightCounter;
import io.datappeal.spring.off.shutdown.Shutdowner;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@SpringBootTest(
        classes = {
                SpringSignalHandlerIntegrationTest.TestSpringApplication.class,
                SpringSignalHandlerIntegrationTest.TestBeans.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SpringSignalHandlerIntegrationTest {

    private RestTemplate client = new RestTemplate();

    @LocalServerPort
    private int port;

    @Autowired
    private TestableSignalListener testableSignalListener;

    @MockBean
    private Shutdowner shutdowner;

    @MockBean
    private InFlightCounter inFlightCounter;

    @Test
    public void shouldAnswerReadyAndThenNotReadyWhenTriggered() {
        final String readyUrl = String.format("http://localhost:%d/ready", port);
        final ResponseEntity<String> response = this.client.getForEntity(readyUrl, String.class);
        assertThat(response.getStatusCodeValue())
                .isEqualTo(200);

        this.testableSignalListener.trigger();

        Assertions.assertThrows(
                HttpStatusCodeException.class,
                () -> this.client.getForEntity(readyUrl, String.class)
        );

        Mockito.verify(shutdowner).shutdown();
        Mockito.verify(inFlightCounter, times(2)).incr();
        Mockito.verify(inFlightCounter, times(2)).decr();
    }


    @Test
    public void shouldAnswerReadyWhenCalled() {
        final String readyUrl = String.format("http://localhost:%d/ready", port);
        final ResponseEntity<String> response = this.client.getForEntity(readyUrl, String.class);
        assertThat(response.getStatusCodeValue())
                .isEqualTo(200);
        Mockito.verify(inFlightCounter).incr();
        Mockito.verify(inFlightCounter).decr();
    }

    @Test
    public void shouldAnswer404NotFoundWhenWrongUrlCalled() {
        Assertions.assertThrows(
                HttpStatusCodeException.class, () -> {
                    final String readyUrl = String.format("http://localhost:%d/test_ready", port);
                    final ResponseEntity<String> response = this.client.getForEntity(readyUrl, String.class);
                }
        );
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
