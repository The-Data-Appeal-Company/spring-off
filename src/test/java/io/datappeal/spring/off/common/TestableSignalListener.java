package io.datappeal.spring.off.common;

import io.datappeal.spring.off.signal.SignalListener;

public class TestableSignalListener implements SignalListener {

    private Runnable onSignalReceived;

    @Override
    public void listen(final Runnable onSignalReceived) {
        this.onSignalReceived = onSignalReceived;
    }

    public void trigger() {
        this.onSignalReceived.run();
    }

}
