package io.datappeal.spring.off;

import io.datappeal.spring.off.configuration.GracefulShutdownConfiguration;
import io.datappeal.spring.off.controller.ReadyHandlerController;
import io.datappeal.spring.off.filter.AtomicInFlightCounter;
import io.datappeal.spring.off.filter.InFlightRequestsFilter;
import io.datappeal.spring.off.shutdown.SpringShutdowner;
import io.datappeal.spring.off.signal.RuntimeSignalListener;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {
        GracefulShutdownConfiguration.class,
        RuntimeSignalListener.class,
        SpringShutdowner.class,
        InFlightRequestsFilter.class,
        AtomicInFlightCounter.class,
        ReadyHandlerController.class,
})
public @interface GracefulShutdown {
}
