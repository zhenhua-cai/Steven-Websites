package net.stevencai.blog.backend.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

public interface Post extends Serializable {
    String getId();

    String getTitle();

    String getSummary();

    String getPath();

    LocalDateTime getCreateDateTime();

    LocalDateTime getLastModifiedDateTime();

    User getUser();

    boolean isPrivateMode();

    static SortArticleOrderByField valueOf(String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "title":
                return SortArticleOrderByField.TITLE;
            case "createdate":
                return SortArticleOrderByField.CREATE_DATE;
            case "privatemode":
                return SortArticleOrderByField.PRIVATE_MODE;
            case "lastmodified":
                return SortArticleOrderByField.LAST_MODIFIED;
            default:
                return SortArticleOrderByField.OTHER;
        }
    }
}
