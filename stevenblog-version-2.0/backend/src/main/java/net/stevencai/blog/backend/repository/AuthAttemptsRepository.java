package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.AuthAttempts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface AuthAttemptsRepository {
    int getAttempts(String ip);
    void clearAttempts(String ip);

    boolean okForNextAttempts(String ip);

    boolean needsToNotifyOwnerAfterIncrease(String ip);
}
