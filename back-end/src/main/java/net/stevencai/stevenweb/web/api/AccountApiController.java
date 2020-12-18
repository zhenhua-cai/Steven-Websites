package net.stevencai.stevenweb.web.api;

import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.service.AccountService;
import net.stevencai.stevenweb.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/account")
public class AccountApiController {
    private AccountService accountService;
    private EmailService emailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * This method is for testing purpose right now. This method expose csrf token to users.
     * It has potential security issue.
     * Might be removed after.
     * TODO: evaluate if we still need this method.
     * @param request
     * @return
     */
    @GetMapping("/csrf-token")
    public String getCsrfToken(HttpServletRequest request){
        CsrfToken token = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
        return token.getToken();
    }

    @PostMapping("/resendVerificationEmail")
    public ResponseMessage resendVerificationEmail(@RequestBody RequestObject obj,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){
        User user = accountService.findUserByEmail(obj.email);
        if(user == null){
            return new ResponseMessage(false);
        }
        emailService.resendVerification(user, request.getContextPath()+"/account/registrationConfirm");
        return new ResponseMessage();
    }

    @PostMapping("/resendResetPasswordEmail")
    public ResponseMessage resendResetPasswordEmail(@RequestBody RequestObject obj,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response){
        User user = null;
        if(obj.username == null) {
            user = accountService.findUserByEmail(obj.email);
        }
        else if(obj.email == null){
            user = accountService.findUserByUsername(obj.username);
        }

        if(user == null){
            return new ResponseMessage(false);
        }
        emailService.resendVerification(user, request.getContextPath()+"/account/registrationConfirm");
        return new ResponseMessage();
    }

    private static class RequestObject{
        private String email;
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ResponseMessage{
            private boolean result;

            public ResponseMessage() {
                result = true;
            }

            public ResponseMessage(boolean result) {
                this.result = result;
            }

            public boolean isResult() {
                return result;
            }

            public void setResult(boolean result) {
                this.result = result;
            }
        }
}
