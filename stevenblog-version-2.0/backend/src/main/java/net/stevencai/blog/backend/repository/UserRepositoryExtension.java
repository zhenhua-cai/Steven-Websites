package net.stevencai.blog.backend.repository;

public interface UserRepositoryExtension {
    void lockAccount(String username);

    void unlockAccount(String username);

    void enableAccount(String username);
}
