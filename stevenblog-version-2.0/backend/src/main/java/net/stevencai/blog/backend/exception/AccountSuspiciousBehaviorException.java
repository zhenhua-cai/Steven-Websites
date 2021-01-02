package net.stevencai.blog.backend.exception;

public class AccountSuspiciousBehaviorException extends RuntimeException{
    public AccountSuspiciousBehaviorException() {
    }

    public AccountSuspiciousBehaviorException(String message) {
        super(message);
    }

    public AccountSuspiciousBehaviorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountSuspiciousBehaviorException(Throwable cause) {
        super(cause);
    }

    public AccountSuspiciousBehaviorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
