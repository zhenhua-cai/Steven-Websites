package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.AuthAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AuthAttemptsRepositoryImpl implements AuthAttemptsRepository {

    private final int MAX_ATTEMPTS;
    private final int ATTEMPTS_MIN_DURATION;

    private RedisTemplate<String, Object> redis;

    @Autowired
    public AuthAttemptsRepositoryImpl(@Value("${AUTH_MAX_ATTEMPTS}") int MAX_ATTEMPTS,
                                      @Value("${AUTH_ATTEMPTS_MIN_DURATION_AFTER_FAIL}") int ATTEMPTS_MIN_DURATION) {
        this.MAX_ATTEMPTS = MAX_ATTEMPTS;
        this.ATTEMPTS_MIN_DURATION = ATTEMPTS_MIN_DURATION;
    }

    @Autowired
    public void setRedis(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    @Override
    public int getAttempts(String ip) {
        final AuthAttempts authAttempts = getAuthAttemptsObj(ip);
        return authAttempts == null ? 0 : authAttempts.getAttempts();
    }

    @Override
    public void increaseAttempts(String ip) {
        AuthAttempts authAttempts = getAuthAttemptsObj(ip);
        if (authAttempts == null) {
            authAttempts = new AuthAttempts();
        }
        authAttempts.setAttempts(authAttempts.getAttempts() + 1);
        setAuthAttemptsObj(ip, authAttempts);
    }

    @Override
    public void clearAttempts(String ip) {
        redis.delete(ip);
    }

    /**
     * check if user still be able to attempt login.
     * if user has remaining attempts: MAX_ATTEMPTS - attempts;
     * or last attempts was 2 hours ago.
     *
     * @param ip user login ip address.
     * @return true if ok. false otherwise.
     */
    @Override
    public boolean okForNextAttempts(String ip) {
        final AuthAttempts authAttempts = getAuthAttemptsObj(ip);
        if (authAttempts == null) {
            return true;
        }
        boolean hasRemainingAttempts = authAttempts.getAttempts() < MAX_ATTEMPTS;
        if (hasRemainingAttempts) {
            return true;
        }
        boolean lastAttemptWas2HoursAgo = authAttempts.getLastAttempt()
                .plusHours(ATTEMPTS_MIN_DURATION)
                .isBefore(LocalDateTime.now());
        if (lastAttemptWas2HoursAgo) {
            clearAttempts(ip);
        }
        return lastAttemptWas2HoursAgo;
    }

    private AuthAttempts getAuthAttemptsObj(String ip) {
        return (AuthAttempts) redis.opsForValue().get(ip);
    }

    private void setAuthAttemptsObj(String ip, AuthAttempts authAttempts) {
        redis.opsForValue().set(ip, authAttempts);
    }

}
