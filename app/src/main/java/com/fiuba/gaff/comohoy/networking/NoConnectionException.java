package com.fiuba.gaff.comohoy.networking;

public class NoConnectionException extends Exception {
    private static final String MESSAGE = "Internet connection not found";

    public NoConnectionException() {
        super(MESSAGE);
    }
}
