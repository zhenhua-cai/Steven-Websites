package net.stevencai.stevenweb.web.controller;

import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.entity.VerificationToken;
import net.stevencai.stevenweb.entity.VerificationTokenType;
import net.stevencai.stevenweb.exception.EmailAlreadyExistException;
import net.stevencai.stevenweb.exception.UsernameAlreadyExistException;
import net.stevencai.stevenweb.frontendResource.ChangeEmailForm;
import net.stevencai.stevenweb.frontendResource.ResetPasswordForm;
import net.stevencai.stevenweb.frontendResource.UserResource;
import net.stevencai.stevenweb.service.AccountService;
import net.stevencai.stevenweb.service.EmailService;
import net.stevencai.stevenweb.util.AppUtil;
import net.stevencai.stevenweb.validation.ValidPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;

@Controller
@RequestMapping("/account")
public class AccountController {
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

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userResource", new UserResource());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid UserResource userResource, BindingResult bindingResult,
                                      HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            User user = accountService.createUser(userResource);
            emailService.sendVerification(user, request.getContextPath()+"/account/registrationConfirm");
        } catch (UsernameAlreadyExistException | EmailAlreadyExistException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("userResource", userResource);
            return "registration";
        }
        model.addAttribute("email", userResource.getEmail());
        model.addAttribute("requestEmail", userResource.getEmail());
        model.addAttribute("requestURL", request.getContextPath()+"/api/account/resendVerificationEmail");
        return "verify-email";
    }

    @GetMapping("/registrationConfirm")
    public String registrationConfirm(WebRequest request, @RequestParam("token") String token,
                                      RedirectAttributes redirectAttributes,
                                      Model model
                                      ){
        VerificationToken verificationToken = accountService.findVerificationTokenByToken(token);
        if(verificationToken == null
            || verificationToken.getType() != VerificationTokenType.ACCOUNT_VERIFY){
            String message = "Invalid verification link";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:errorPage";
        }
        if(verificationToken.getExpireDate().isBefore(LocalDateTime.now())){
            String message = "Verification Link expired";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:errorPage";
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        accountService.saveVerifiedUser(user,verificationToken);
        model.addAttribute("message","Email Verified");
        return "confirmation";
    }

    @GetMapping("/errorPage")
    public String invalidUer(Model model){
        if(!model.containsAttribute("message")){
            throw new RuntimeException("Page Is Missing");
        }
        return "errorPage";
    }

    @GetMapping("/requestToResetPassword")
    public String showResetPasswordRequestForm(Model model) {
        return "reset-password-request";
    }

    @PostMapping("resetPassword")
    public String processResetRequest(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      HttpServletRequest request,
                                      Model model) {
        User user = null;
        if(username != null && !username.isEmpty()) {
            user = accountService.findUserByUsername(username);
        }
        else if(email != null && !email.isEmpty()){
            user = accountService.findUserByEmail(email);
        }
        if(user != null){
            VerificationToken tokenCreatedBefore =
                    accountService.findVerificationTokenBefore(user, LocalDateTime.now().minusMinutes(1));
            if(tokenCreatedBefore != null){
                model.addAttribute("message","You have too many requests in a short time, Please try again later");
                return "reset-password-request";
            }
            emailService.sendResetPassword(user,request.getContextPath()+"/account/resetPassword");
            model.addAttribute("resetPassword", true);
            model.addAttribute("email", AppUtil.maskEmail(user.getEmail()));
            if(email != null && !email.isEmpty()) {
                model.addAttribute("requestEmail", email);
            }
            if(username != null && !username.isEmpty()){
                model.addAttribute("requestUsername", username);
            }
            model.addAttribute("requestURL", request.getContextPath()+"/api/account/resendResetPasswordEmail");
            return "verify-email";
        }
        model.addAttribute("message","Cannot find account");
        return "reset-password-request";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(RedirectAttributes redirectAttributes,
                                        @RequestParam("token") String token,
                                        Model model) {
        VerificationToken verificationToken = accountService.findVerificationTokenByToken(token);
        String result = checkResetPasswordToken(verificationToken, redirectAttributes);
        if(result != null){
            return result;
        }
        ResetPasswordForm form = new ResetPasswordForm();
        form.setToken(token);
        model.addAttribute("resetPasswordForm",form);
        return "reset-password";
    }

    @PostMapping("/processResetPassword")
    public String processResetPassword(@Valid ResetPasswordForm form,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       Model model){
        if(bindingResult.hasErrors()){
            return "reset-password";
        }
        VerificationToken verificationToken = accountService.findVerificationTokenByToken(form.getToken());
        String result = checkResetPasswordToken(verificationToken, redirectAttributes);
        if(result != null){
            return result;
        }
        User user = verificationToken.getUser();
        accountService.removeVerificationToken(verificationToken);
        accountService.updateUserPassword(user, form.getPassword());
        model.addAttribute("message","Successfully Reset Password");
        return "confirmation";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex){
        ex.printStackTrace();
        return "pageIsMissing";
    }

    private String checkResetPasswordToken(VerificationToken verificationToken, RedirectAttributes redirectAttributes){

        if(verificationToken == null
                || verificationToken.getType() != VerificationTokenType.RESET_PASSWORD){
            String message = "Invalid link";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:errorPage";
        }

        if(verificationToken.getExpireDate().isBefore(LocalDateTime.now())){
            String message = "This Link expired";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:errorPage";
        }
        return null;
    }
}
