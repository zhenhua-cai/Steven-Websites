package net.stevencai.blog.backend.exception;

public class UsernameAlreadyExistException extends RuntimeException{
    public UsernameAlreadyExistException() {
    }

    public UsernameAlreadyExistException(String message) {
        super(message);
    }

    public UsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public UsernameAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
