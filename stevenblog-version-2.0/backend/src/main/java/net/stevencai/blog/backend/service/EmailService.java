package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.User;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendUserAuthAlert(String username, String ip);

    @Async
    void sendUserEmailVerificationCode(User user);

    @Async
    void sendUserUsernameEmail(User user);

    @Async
    void sendUserResetPasswordEmail(User user);
}
