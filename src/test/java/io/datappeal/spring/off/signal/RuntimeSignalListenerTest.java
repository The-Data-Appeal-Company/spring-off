package io.datappeal.spring.off.signal;

import org.junit.jupiter.api.Test;

class RuntimeSignalListenerTest {

    @Test
    void shouldRegisterListenerWithoutException() {
        final RuntimeSignalListener listener = new RuntimeSignalListener();
        listener.listen(null);
    }

}
