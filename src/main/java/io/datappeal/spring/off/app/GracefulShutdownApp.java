package io.datappeal.spring.off.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public final class GracefulShutdownApp {

    private GracefulShutdownApp() {
    }

    public static ConfigurableApplicationContext run(final Class<?> clazz, final String... args) {
        return new SpringApplicationBuilder(clazz)
                .registerShutdownHook(false)
                .build()
                .run(args);
    }

    public static ConfigurableApplicationContext run(final SpringApplication application, final String... args) {
        application.setRegisterShutdownHook(false);
        return application.run(args);
    }

}
