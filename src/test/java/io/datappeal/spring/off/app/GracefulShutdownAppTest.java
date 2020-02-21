package io.datappeal.spring.off.app;

import io.datappeal.spring.off.GracefulShutdown;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

class GracefulShutdownAppTest {

    @Test
    public void testGracefulShutdownAppLauncherWithClass() {
        final ConfigurableApplicationContext run = GracefulShutdownApp.run(GracefulShutdownAppTest.TestSpringApplicationAnnotated.class);
    }

    @Test
    public void testGracefulShutdownAppLauncherWithInstance() {
        final SpringApplication app = new SpringApplicationBuilder(TestSpringApplicationAnnotated.class)
                .web(WebApplicationType.NONE)
                .build();
        final ConfigurableApplicationContext run = GracefulShutdownApp.run(app);
    }

    @GracefulShutdown
    @SpringBootApplication
    public static class TestSpringApplicationAnnotated {
    }

}
