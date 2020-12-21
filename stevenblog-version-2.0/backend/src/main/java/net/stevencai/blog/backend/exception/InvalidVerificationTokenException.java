package net.stevencai.blog.backend.exception;

public class InvalidVerificationTokenException extends RuntimeException{
    public InvalidVerificationTokenException() {
    }

    public InvalidVerificationTokenException(String message) {
        super(message);
    }

    public InvalidVerificationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidVerificationTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidVerificationTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
