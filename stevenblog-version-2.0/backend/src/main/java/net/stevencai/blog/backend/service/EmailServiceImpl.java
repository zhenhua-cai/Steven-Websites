package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.exception.SendingEmailFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;
    private AccountService accountService;

    @Value("${AUTH_ATTEMPTS_MIN_DURATION_AFTER_FAIL}")
    private int blockHours;
    private SpringTemplateEngine thymeleaf;

    @Autowired
    public void setThymeleaf(SpringTemplateEngine thymeleaf) {
        this.thymeleaf = thymeleaf;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendUserAuthAlert(String username, String ip) {
        User user = accountService.findUserByUsername(username);
        if (user == null) {
            return;
        }
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", user.getUsername());
        attributes.put("ipAddress", ip);
        attributes.put("loginTime", LocalDateTime.now());
        attributes.put("blockHours", blockHours);
        sendEmail("UserAccountAlert", attributes, user.getEmail(), "Account Login Alert");
    }

    private void sendEmail(String templateName, Map<String, Object> attributes, String sendTo, String subject) throws SendingEmailFailException {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String emailText = thymeleaf.process(templateName, context);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setText(emailText, true);
            helper.setTo(sendTo);
            helper.setSubject(subject);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new SendingEmailFailException("Fail to send email to " + sendTo, e);
        }
    }
}
