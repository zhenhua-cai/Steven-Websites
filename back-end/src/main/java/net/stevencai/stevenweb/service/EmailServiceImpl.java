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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:application-config.properties")
public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;
    private Environment env;
    private AppUtil appUtil;
    private SpringTemplateEngine thymeleaf;
    private AccountService accountService;

    @Autowired
    public void setThymeleaf(SpringTemplateEngine thymeleaf) {
        this.thymeleaf = thymeleaf;
    }

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
        Map<String, Object> attributes= new HashMap<>();
        attributes.put("link",link);
        attributes.put("token",token);
        sendEmail("mail/verificationEmail",attributes, user.getEmail(),"Account Activation");
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
        Map<String, Object> attributes= new HashMap<>();
        attributes.put("link",link);
        attributes.put("token",token);
        attributes.put("username",user.getUsername());
        sendEmail("mail/resetPassword",attributes, user.getEmail(),"Reset Password");

    }
    private void sendEmail(String templateName, Map<String, Object> attribute,
                           String sendTo, String subject){
        Context context = new Context();
        for(Map.Entry<String, Object> entry: attribute.entrySet()){
            context.setVariable(entry.getKey(),entry.getValue());
        }
        String emailText = thymeleaf.process(templateName,context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setText(emailText,true);
            helper.setTo(sendTo);
            helper.setSubject(subject);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new SendingEmailFailException("Fail to send email to "+sendTo);
        }

    }

    private String createVerificationLink(String path, String token){
        return env.getProperty("app.domain") + path + "?token=" + token;
    }


}
