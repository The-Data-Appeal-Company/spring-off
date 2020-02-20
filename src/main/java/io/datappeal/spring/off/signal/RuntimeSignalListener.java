package io.datappeal.spring.off.signal;

public class RuntimeSignalListener implements SignalListener {

    private RuntimeSignalListener() {
    }

    @Override
    public void listen(final Runnable onSignalReceived) {
        Runtime.getRuntime()
                .addShutdownHook(new Thread(onSignalReceived));
    }
}
