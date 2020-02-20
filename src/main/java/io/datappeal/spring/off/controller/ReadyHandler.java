package io.datappeal.spring.off.controller;

public interface ReadyHandler {

    boolean isReady();

    void setReadiness(boolean ready);

}
