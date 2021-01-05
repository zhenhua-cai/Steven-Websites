package net.stevencai.blog.backend.exception;

public class IpBlockedException extends RuntimeException{
    public IpBlockedException() {

    }

    public IpBlockedException(String message) {
        super(message);
    }

    public IpBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IpBlockedException(Throwable cause) {
        super(cause);
    }

    public IpBlockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
