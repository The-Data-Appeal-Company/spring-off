package io.datappeal.spring.off.signal;

public interface SignalListener {
    void listen(Runnable onSignalReceived);
}
