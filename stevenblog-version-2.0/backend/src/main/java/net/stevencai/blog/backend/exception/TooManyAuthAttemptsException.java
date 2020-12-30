package net.stevencai.blog.backend.exception;

public class TooManyAuthAttemptsException extends RuntimeException{
    public TooManyAuthAttemptsException() {
    }

    public TooManyAuthAttemptsException(String message) {
        super(message);
    }

    public TooManyAuthAttemptsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyAuthAttemptsException(Throwable cause) {
        super(cause);
    }

    public TooManyAuthAttemptsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
