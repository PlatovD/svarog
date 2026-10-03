package io.github.PlatovD.svarog.exception;

public class AmbiguousBeanException extends SvarogException {

    public AmbiguousBeanException(String message) {
        super(message);
    }

    public AmbiguousBeanException(String message, Throwable cause) {
        super(message, cause);
    }
}