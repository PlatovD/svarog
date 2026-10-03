package io.github.PlatovD.svarog.exception;

public class CircularDependencyException extends SvarogException {

    public CircularDependencyException(String message) {
        super(message);
    }

    public CircularDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}