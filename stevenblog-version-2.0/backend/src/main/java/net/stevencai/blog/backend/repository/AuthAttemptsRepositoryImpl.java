package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.AuthAttempts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AuthAttemptsRepositoryImpl implements AuthAttemptsRepository {


    private RedisTemplate<String, Object> redis;

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
    public void clearAttempts(String ip) {
        redis.delete(ip);
    }

    @Override
    public LocalDateTime getLastAttempt(String ip) {
        final AuthAttempts authAttempts = getAuthAttemptsObj(ip);
        if (authAttempts == null) {
            return null;
        }
        return authAttempts.getLastAttempt();
    }

    @Override
    public void setAuthAttempts(String ip, AuthAttempts authAttempts) {
        this.setAuthAttemptsObj(ip, authAttempts);
    }

    @Override
    public AuthAttempts getAuthAttempts(String ip) {
        return getAuthAttemptsObj(ip);
    }

    private AuthAttempts getAuthAttemptsObj(String ip) {
        return (AuthAttempts) redis.opsForValue().get(ip);
    }

    private void setAuthAttemptsObj(String ip, AuthAttempts authAttempts) {
        redis.opsForValue().set(ip, authAttempts);
    }

    @Override
    public int increaseAuthAttempts(String ip) {
        AuthAttempts authAttempts = getAuthAttemptsObj(ip);
        if (authAttempts == null) {
            authAttempts = new AuthAttempts();
        }
        authAttempts.setAttempts(authAttempts.getAttempts() + 1);
        setAuthAttemptsObj(ip, authAttempts);
        return authAttempts.getAttempts();
    }

}
