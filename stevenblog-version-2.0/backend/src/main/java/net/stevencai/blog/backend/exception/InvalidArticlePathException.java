package net.stevencai.blog.backend.exception;

public class InvalidArticlePathException extends RuntimeException{
    public InvalidArticlePathException() {
    }

    public InvalidArticlePathException(String message) {
        super(message);
    }

    public InvalidArticlePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArticlePathException(Throwable cause) {
        super(cause);
    }

    public InvalidArticlePathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
