package net.stevencai.blog.backend.exception;

public class TooManyVerifyAttemptsException extends RuntimeException{
    public TooManyVerifyAttemptsException() {
    }

    public TooManyVerifyAttemptsException(String message) {
        super(message);
    }

    public TooManyVerifyAttemptsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyVerifyAttemptsException(Throwable cause) {
        super(cause);
    }

    public TooManyVerifyAttemptsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
