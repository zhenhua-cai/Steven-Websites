package net.stevencai.blog.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    boolean isOkForNextAuthAttempt(String ip);
    void increaseAuthAttempts(String ip);

    void clearAuthAttempts(String remoteAddr);
}
