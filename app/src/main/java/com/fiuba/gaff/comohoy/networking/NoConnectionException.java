package com.fiuba.gaff.comohoy.networking;

public class NoConnectionException extends Exception {
    private static final String MESSAGE = "Internet onnection not found";

    public NoConnectionException() {
        super(MESSAGE);
    }
}
