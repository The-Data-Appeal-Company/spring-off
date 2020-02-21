package io.datappeal.spring.off.configuration;

import io.datappeal.spring.off.SpringSignalHandler;
import io.datappeal.spring.off.filter.InFlightCounter;
import io.datappeal.spring.off.shutdown.Shutdowner;
import io.datappeal.spring.off.signal.SignalListener;

public class GracefulShutdownConfiguration {

    private final SignalListener signalListener;
    private final Shutdowner shutdowner;
    private final InFlightCounter inFlightCounter;

    public GracefulShutdownConfiguration(
            final SignalListener signalListener,
            final Shutdowner shutdowner,
            final InFlightCounter inFlightCounter
    ) {
        this.signalListener = signalListener;
        this.shutdowner = shutdowner;
        this.inFlightCounter = inFlightCounter;

        this.init();
    }

    private void init() {
        final ShutdownConfiguration configuration = new FixedShutdownConfiguration();

        final SpringSignalHandler shutdownHandler = new SpringSignalHandler(
                this.shutdowner,
                this.inFlightCounter,
                configuration
        );

        this.signalListener.listen(shutdownHandler);
    }

}
