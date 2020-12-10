package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.exception.SendingEmailFailException;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import java.util.Locale;

public interface EmailService {
    @Async
    void sendVerification(User user,String path)throws SendingEmailFailException;
    @Async
    void resendVerification(User user,String path)throws SendingEmailFailException;

    @Async
    void sendResetPassword(User user, String path);
}
