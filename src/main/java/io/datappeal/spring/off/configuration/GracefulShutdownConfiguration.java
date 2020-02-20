package io.datappeal.spring.off.configuration;

import io.datappeal.spring.off.SpringSignalHandler;
import io.datappeal.spring.off.controller.ReadyHandlerController;
import io.datappeal.spring.off.filter.InFlightCounter;
import io.datappeal.spring.off.shutdown.Shutdowner;
import io.datappeal.spring.off.signal.SignalListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class GracefulShutdownConfiguration {

    private final ApplicationContext context;
    private final SignalListener signalListener;
    private final Shutdowner shutdowner;
    private final InFlightCounter inFlightCounter;

    public GracefulShutdownConfiguration(
            final ApplicationContext context,
            final SignalListener signalListener,
            final Shutdowner shutdowner,
            final InFlightCounter inFlightCounter
    ) {
        this.context = context;
        this.signalListener = signalListener;
        this.shutdowner = shutdowner;
        this.inFlightCounter = inFlightCounter;

        this.init();
    }

    private void init() {
        final GenericApplicationContext webContext = (GenericApplicationContext) this.context;

        final ReadyHandlerController readyHandlerController = webContext.getBean(
                ReadyHandlerController.class
        );

        final SpringSignalHandler shutdownHandler = new SpringSignalHandler(
                this.shutdowner,
                readyHandlerController,
                this.inFlightCounter
        );

        this.signalListener.listen(shutdownHandler);

    }

}
