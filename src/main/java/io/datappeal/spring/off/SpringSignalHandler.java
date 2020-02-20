package io.datappeal.spring.off;

import io.datappeal.spring.off.controller.ReadyHandler;
import io.datappeal.spring.off.deadline.Deadline;
import io.datappeal.spring.off.filter.InFlightCounter;
import io.datappeal.spring.off.shutdown.Shutdowner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SpringSignalHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SpringSignalHandler.class);

    private final Shutdowner shutdowner;
    private final ReadyHandler readyHandler;
    private final InFlightCounter inFlightCounter;

    public SpringSignalHandler(Shutdowner shutdowner, ReadyHandler readyHandler, InFlightCounter inFlightCounter) {
        this.shutdowner = shutdowner;
        this.readyHandler = readyHandler;
        this.inFlightCounter = inFlightCounter;
    }

    @Override
    public void run() {
        logger.info("received termination signal, stopping ready controller");
        this.readyHandler.setReadiness(false);

        // TODO Add fixed delay


        final Deadline deadline = Deadline.of(30, TimeUnit.SECONDS).fromNow(); // TODO parametrize duration

        while (!deadline.isExpired()) {
            if (this.inFlightCounter.inFlight() == 0) {
                break;
            }

            logger.info("waiting for {} requests", this.inFlightCounter.inFlight());
            sleep(deadline.timeLeft() / 4);
        }

        logger.info("graceful termination completed, shutting down spring");
        this.shutdowner.shutdown();
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
