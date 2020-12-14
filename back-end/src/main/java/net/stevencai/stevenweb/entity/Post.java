package net.stevencai.stevenweb.entity;

public interface Post {
    String getId();

    Object getTitle();

    Object getPath();

    Object getCreateDateTime();

    Object getLastModifiedDateTime();

    User getUser();
}
