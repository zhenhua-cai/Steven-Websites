package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.AuthAttempts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

public interface AuthAttemptsRepository {
    int getAttempts(String ip);

    void clearAttempts(String ip);

    int increaseAuthAttempts(String ip);

    LocalDateTime getLastAttempt(String ip);

    void setAuthAttempts(String ip, AuthAttempts authAttempts);

    AuthAttempts getAuthAttempts(String ip);
}
