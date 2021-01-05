package net.stevencai.blog.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    boolean isIpNonBlocked(String ip);

    boolean needsToNotifyOwnerAfterAuthFail(String ip);

    void clearAuthAttempts(String remoteAddr);

    void lockIpAddress(String remoteAddr);

    void unlockIpAddress(String remoteAddr);
}
