package com.name.client.exceptions;

public class ServerSideException extends Exception {
    public ServerSideException() {
        super();
    }

    public ServerSideException(String message) {
        super(message);
    }

    public ServerSideException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerSideException(Throwable cause) {
        super(cause);
    }
}
