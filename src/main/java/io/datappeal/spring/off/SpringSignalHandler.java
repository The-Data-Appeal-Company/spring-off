package io.datappeal.spring.off;

import io.datappeal.spring.off.configuration.ShutdownConfiguration;
import io.datappeal.spring.off.deadline.Deadline;
import io.datappeal.spring.off.filter.InFlightCounter;
import io.datappeal.spring.off.shutdown.Shutdowner;
import io.datappeal.spring.off.signal.RuntimeSignalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SpringSignalHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SpringSignalHandler.class);

    private final Shutdowner shutdowner;
    private final InFlightCounter inFlightCounter;
    private final ShutdownConfiguration configuration;

    public SpringSignalHandler(
            final Shutdowner shutdowner,
            final InFlightCounter inFlightCounter,
            final ShutdownConfiguration configuration
    ) {
        this.shutdowner = shutdowner;
        this.inFlightCounter = inFlightCounter;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        logger.info("received termination signal, stopping ready controller");

        this.sleepWithDefer(configuration.shutdownBaseDelay(), shutdowner::shutdown);

        final Deadline deadline = Deadline.of(configuration.shutdownMaxDeadline(), TimeUnit.MILLISECONDS).fromNow();

        while (!deadline.isExpired()) {
            if (this.inFlightCounter.inFlight() == 0) {
                break;
            }

            logger.info("waiting for {} requests", this.inFlightCounter.inFlight());
            this.sleepWithDefer(deadline.timeLeft() / 4, shutdowner::shutdown);
        }

        logger.info("graceful termination completed, shutting down spring");
        this.shutdowner.shutdown();
    }

    private void sleepWithDefer(long ms, final Runnable onInterrupt) {
        try {
            Thread.sleep(ms);
        } catch (final InterruptedException e) {
            onInterrupt.run();
            throw new RuntimeException(e);
        }
    }
}
