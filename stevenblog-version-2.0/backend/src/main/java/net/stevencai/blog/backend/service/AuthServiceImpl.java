package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.AuthAttempts;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.repository.UserRepository;
import net.stevencai.blog.backend.repository.AuthAttemptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AuthServiceImpl implements AuthService {
    private final int MAX_ATTEMPTS;
    private UserRepository userRepository;
    private AuthAttemptsRepository authAttemptsRepository;
    private final int ATTEMPTS_MIN_DURATION;

    public AuthServiceImpl(@Value("${auth.max.attempts}") int MAX_ATTEMPTS,
                           @Value("${auth.min.duration.after.fail}") int ATTEMPTS_MIN_DURATION) {
        this.MAX_ATTEMPTS = MAX_ATTEMPTS;
        this.ATTEMPTS_MIN_DURATION = ATTEMPTS_MIN_DURATION;
    }

    @Autowired
    public void setAuthAttemptsRepository(AuthAttemptsRepository authAttemptsRepository) {
        this.authAttemptsRepository = authAttemptsRepository;
    }

    @Autowired
    public void setAccountRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't exist");
        }
        return user;
    }

    @Override
    public boolean isIpNonBlocked(String ip) {
        int attempts = authAttemptsRepository.getAttempts(ip);
        if (attempts <= MAX_ATTEMPTS) {
            return true;
        }
        LocalDateTime lastAttempt = authAttemptsRepository.getLastAttempt(ip);
        boolean ableToAttempt = lastAttempt.plusHours(ATTEMPTS_MIN_DURATION)
                .isBefore(LocalDateTime.now(ZoneOffset.UTC));
        if (ableToAttempt) {
            this.clearAuthAttempts(ip);
        }
        return ableToAttempt;
    }

    @Override
    public boolean needsToNotifyOwnerAfterAuthFail(String ip) {
        int attempts = authAttemptsRepository.increaseAuthAttempts(ip);
        return attempts > MAX_ATTEMPTS;
    }

    @Override
    public void clearAuthAttempts(String remoteAddr) {
        authAttemptsRepository.clearAttempts(remoteAddr);
    }

    @Override
    public void lockIpAddress(String remoteAddr) {
        AuthAttempts authAttempts = new AuthAttempts();
        authAttempts.setAttempts(MAX_ATTEMPTS + 1);
        authAttemptsRepository.setAuthAttempts(remoteAddr, authAttempts);
    }

    @Override
    public void unlockIpAddress(String remoteAddr) {
        this.clearAuthAttempts(remoteAddr);
    }

}
