package net.stevencai.stevenweb.exception;

public class SendingEmailFailException extends RuntimeException{
    public SendingEmailFailException() {
    }

    public SendingEmailFailException(String message) {
        super(message);
    }

    public SendingEmailFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingEmailFailException(Throwable cause) {
        super(cause);
    }

    public SendingEmailFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
