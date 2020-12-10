package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.entity.VerificationTokenType;
import net.stevencai.stevenweb.exception.SendingEmailFailException;
import net.stevencai.stevenweb.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:application-config.properties")
public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;
    private Environment env;
    private AppUtil appUtil;

    private AccountService accountService;

    @Autowired
    public void setAppUtil(AppUtil appUtil) {
        this.appUtil = appUtil;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendVerification(User user, String path)throws SendingEmailFailException{
        String token = appUtil.createVerificationToken(user);
        accountService.createVerificationTokenForUser(user,token,VerificationTokenType.ACCOUNT_VERIFY);
        String link =createVerificationLink(path,token);
        sendEmail(user.getEmail(), link, "Account Activation");
    }

    @Override
    @Async
    public void resendVerification(User user, String path) throws SendingEmailFailException{
        accountService.removeAllVerificationTokenForUser(user);
        sendVerification(user, path);
    }

    @Override
    public void sendResetPassword(User user, String path) {
        String token = appUtil.createVerificationToken(user);
        accountService.createVerificationTokenForUser(user,token,VerificationTokenType.RESET_PASSWORD);
        String link =createVerificationLink(path,token);
        sendEmail(user.getEmail(), link, "Reset Password");
    }

    private void sendEmail(String email , String text, String subject){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new SendingEmailFailException("Fail to send email to "+email);
        }
    }

    private String createVerificationLink(String path, String token){
        return env.getProperty("app.domain") + path + "?token=" + token;
    }


}
