package io.datappeal.spring.off.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadyHandlerController implements ReadyHandler {

    private boolean ready;

    public ReadyHandlerController() {
        this.ready = true;
    }

    @GetMapping("/ready")
    public ResponseEntity<String> ready() {
        final HttpStatus status = isReady() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return new ResponseEntity<>(status);
    }

    @Override
    public boolean isReady() {
        return this.ready;
    }

    public void setReadiness(final boolean state) {
        this.ready = state;
    }

}
