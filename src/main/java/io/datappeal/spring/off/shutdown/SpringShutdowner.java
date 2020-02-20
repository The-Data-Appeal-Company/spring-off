package io.datappeal.spring.off.shutdown;

import org.springframework.context.ConfigurableApplicationContext;

public class SpringShutdowner implements Shutdowner {

    private final ConfigurableApplicationContext context;

    private SpringShutdowner(final ConfigurableApplicationContext context) {
        this.context = context;
    }

    public static SpringShutdowner of(ConfigurableApplicationContext context) {
        return new SpringShutdowner(context);
    }

    @Override
    public void shutdown() {
        this.context.close();
    }

}
