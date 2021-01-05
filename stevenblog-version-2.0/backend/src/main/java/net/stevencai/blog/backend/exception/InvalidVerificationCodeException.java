package net.stevencai.blog.backend.exception;

public class InvalidVerificationCodeException extends RuntimeException{
    public InvalidVerificationCodeException() {
    }

    public InvalidVerificationCodeException(String message) {
        super(message);
    }

    public InvalidVerificationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidVerificationCodeException(Throwable cause) {
        super(cause);
    }

    public InvalidVerificationCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
