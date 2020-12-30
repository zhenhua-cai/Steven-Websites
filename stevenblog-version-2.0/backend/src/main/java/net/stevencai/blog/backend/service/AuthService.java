package net.stevencai.blog.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    boolean isOkForNextAuthAttempt(String ip);

    boolean needsToNotifyOwnerAfterIncrease(String ip);

    void clearAuthAttempts(String remoteAddr);
}
