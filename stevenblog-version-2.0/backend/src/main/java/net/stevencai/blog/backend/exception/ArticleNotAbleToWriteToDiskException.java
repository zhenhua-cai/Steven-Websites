package net.stevencai.blog.backend.exception;

public class ArticleNotAbleToWriteToDiskException extends RuntimeException {
    public ArticleNotAbleToWriteToDiskException() {
    }

    public ArticleNotAbleToWriteToDiskException(String message) {
        super(message);
    }

    public ArticleNotAbleToWriteToDiskException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleNotAbleToWriteToDiskException(Throwable cause) {
        super(cause);
    }

    public ArticleNotAbleToWriteToDiskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
