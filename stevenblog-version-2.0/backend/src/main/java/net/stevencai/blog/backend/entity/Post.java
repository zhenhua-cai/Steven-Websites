package net.stevencai.blog.backend.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface Post extends Serializable {
    String getId();

    String getTitle();

    String getSummary();

    String getPath();

    LocalDateTime getCreateDateTime();

    LocalDateTime getLastModifiedDateTime();

    User getUser();
}
