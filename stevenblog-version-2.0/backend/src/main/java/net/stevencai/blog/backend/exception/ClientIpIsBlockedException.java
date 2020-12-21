package net.stevencai.blog.backend.exception;

public class ClientIpIsBlockedException extends RuntimeException{
    public ClientIpIsBlockedException() {
    }

    public ClientIpIsBlockedException(String message) {
        super(message);
    }

    public ClientIpIsBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientIpIsBlockedException(Throwable cause) {
        super(cause);
    }

    public ClientIpIsBlockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
