package io.datappeal.spring.off;

import io.datappeal.spring.off.app.GracefulShutdownApp;
import io.datappeal.spring.off.common.Sleep;
import io.datappeal.spring.off.common.TestableSignalListener;
import io.datappeal.spring.off.configuration.FixedShutdownConfiguration;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                SpringSignalHandlerTest.TestSpringApplication.class,
                SpringSignalHandlerTest.TestBeans.class,
                SpringSignalHandlerTest.TestController.class,
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class SpringSignalHandlerTest {

    private RestTemplate client = new RestTemplate();

    @LocalServerPort
    private int port;

    @Autowired
    private TestableSignalListener testableSignalListener;

    @MockBean
    private Shutdowner shutdowner;

    @Test
    public void shouldWaitAllCallsBeforeShutdown() throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);

        final List<Future<?>> futures = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            futures.add(executorService.submit(() -> {
                final String readyUrl = String.format("http://localhost:%d/slow", port);
                final ResponseEntity<String> response = this.client.getForEntity(readyUrl, String.class);
                assertThat(response.getStatusCodeValue())
                        .isEqualTo(200);
            }));
        }

        this.testableSignalListener.trigger();
        executorService.shutdown();
        for (Future<?> future : futures) {
            future.get();
        }

        Mockito.verify(shutdowner).shutdown();

    }

    @Test
    void testRunShutdownHandler() {
        final Shutdowner shutdowner = Mockito.mock(Shutdowner.class);
        final InFlightCounter inFlightCounter = Mockito.mock(InFlightCounter.class);

        final SpringSignalHandler signalHandler = new SpringSignalHandler(
                shutdowner,
                inFlightCounter,
                FixedShutdownConfiguration.immediate()
        );

        signalHandler.run();
        Mockito.verify(shutdowner).shutdown();
    }

    @Test
    void testRunShutdownHandlerInterruptException() throws InterruptedException {
        final Shutdowner shutdowner = Mockito.mock(Shutdowner.class);
        final InFlightCounter inFlightCounter = Mockito.mock(InFlightCounter.class);

        final SpringSignalHandler signalHandler = new SpringSignalHandler(
                shutdowner,
                inFlightCounter,
                new FixedShutdownConfiguration(10_000, 10_000)
        );

        final Thread handlerThread = new Thread(signalHandler);
        handlerThread.start();

        handlerThread.interrupt();
        handlerThread.join();

        Mockito.verify(shutdowner).shutdown();
    }


    @GracefulShutdown
    @SpringBootApplication
    public static class TestSpringApplication {
        public static void main(String[] args) {
            GracefulShutdownApp.run(TestSpringApplication.class, args);
        }
    }

    @RestController
    public static class TestController {
        @GetMapping("/slow")
        public String slow() {
            Sleep.sleep(1000);
            return "ok";
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
