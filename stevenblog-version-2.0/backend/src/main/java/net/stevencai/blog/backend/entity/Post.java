package net.stevencai.blog.backend.entity;

import java.time.LocalDateTime;

public interface Post {
    String getId();

    String getTitle();

    String getPath();

    LocalDateTime getCreateDateTime();

    LocalDateTime getLastModifiedDateTime();

    User getUser();
}
