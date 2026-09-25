package io.github.PlatovD.svarog.exception;

public class SvarogException extends RuntimeException {

    public SvarogException(String message) {
        super(message);
    }

    public SvarogException(String message, Throwable cause) {
        super(message, cause);
    }
}
