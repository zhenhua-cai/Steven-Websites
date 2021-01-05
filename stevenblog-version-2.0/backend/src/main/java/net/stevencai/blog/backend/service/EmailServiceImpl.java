package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.exception.SendingEmailFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UtilService utilService;
    private VerificationTokenService verificationTokenService;

    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${auth.min.duration.after.fail}")
    private int blockHours;

    @Value("${steven.blog.domain}")
    private String host;

    private SpringTemplateEngine thymeleaf;

    @Autowired
    public void setVerificationTokenService(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

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
        attributes.put("resetLinnk", generateLink("account/resetPassword"));
        sendEmail("UserAccountAlert", attributes, user.getEmail(), "Account Login Alert");
    }

    @Override
    @Async
    public void sendUserEmailVerificationCode(User user) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", user.getUsername());
        String verificationCode = this.utilService.generateVerificationCode(6);
        this.verificationTokenService.saveNewToken(user.getUsername(), verificationCode);
        attributes.put("code", verificationCode);
        sendEmail("activateAccount", attributes, user.getEmail(), "Activate Account");
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
            logger.error("Fail to send email to " + sendTo, e);
            //TODO: save email address and resend later
        }
    }

    private String generateLink(String path) {
        return host + path;
    }
}
