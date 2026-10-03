package io.github.PlatovD.svarog.exception;

public class NoSuchBeanException extends SvarogException {

    public NoSuchBeanException(String message) {
        super(message);
    }

    public NoSuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
