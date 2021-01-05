package net.stevencai.blog.backend.api;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.AccountEmailVerifyObj;
import net.stevencai.blog.backend.clientResource.ApplicationUser;
import net.stevencai.blog.backend.clientResource.SignUpUser;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.entity.VerificationToken;
import net.stevencai.blog.backend.exception.*;
import net.stevencai.blog.backend.response.ActionStatusResponse;
import net.stevencai.blog.backend.response.AuthResponse;
import net.stevencai.blog.backend.response.SignUpResponse;
import net.stevencai.blog.backend.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthApi {

    private AuthenticationManager authenticationManager;
    private AccountService accountService;
    private JwtService jwtService;
    private AuthService authService;
    private EmailService emailService;
    private UtilService utilService;
    private VerificationTokenService verificationTokenService;
    private final Logger logger = LoggerFactory.getLogger(AuthApi.class);


    @Autowired
    public void setVerificationTokenService(VerificationTokenService verificationTokenService) {
        this.verificationTokenService = verificationTokenService;
    }

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/accessToken")
    public AuthResponse getAccessToken(@RequestBody RefreshToken refreshTokenObj,
                                       HttpServletRequest request) {
        if (refreshTokenObj == null || !refreshTokenObj.getRefreshToken().startsWith("Bearer ")) {
            throw new IllegalArgumentException("JWT is missing");
        }
        String ip = this.utilService.getClientIp(request);
        if (!authService.isIpNonBlocked(ip)) {
            throw new IpBlockedException();
        }
        String jwt = refreshTokenObj.getRefreshToken().substring(7);
        String username = jwtService.getUsernameFromJwt(jwt);
        if (username == null) {
            throw new IllegalArgumentException("Invalid JWT");
        }
        // check if account is locked before refresh token.
        User user = this.accountService.findUserByUsername(username);
        if (user.isAccountLocked()) {
            throw new LockedException("Account was locked due to suspicious behaviour");
        }
        // check if refresh token was already replace with new one.
        // if so, that might implies that request was send from a token theft.
        // notify user immediately.
        if (jwtService.isRefreshTokenBlocked(username, refreshTokenObj.refreshToken)) {
            logger.error("Account has suspicious behavior: User attempted to login with deprecated token from " + ip);
            this.accountService.lockAccount(username);
            this.emailService.sendUserAuthAlert(username, ip);
            throw new AccountSuspiciousBehaviorException("Attempted to login with banned refresh token.");
        }
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);
        // uncomment this until solve IPv6 problem.
//        // associate access token with ip. if request ip not matches ip in access token. authentication fails.
//        this.jwtService.associateAccessToken(accessToken, ip);
        this.jwtService.blockDeprecatedRefreshToken(username, refreshToken);
        return new AuthResponse(refreshToken, accessToken);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody(required = false) ApplicationUser applicationUser,
                              HttpServletRequest request) {
        String ip = utilService.getClientIp(request);
        if (!authService.isIpNonBlocked(ip)) {
            throw new IpBlockedException();
        }
        try {
            authenticate(applicationUser.getUsername(), applicationUser.getPassword());
        } catch (BadCredentialsException ex) {
            if (authService.needsToNotifyOwnerAfterAuthFail(ip)) {
                this.emailService.sendUserAuthAlert(applicationUser.getUsername(), ip);
            }
            throw ex;
        } catch (DisabledException ex) {
            User user = this.accountService.findUserByUsername(applicationUser.getUsername());
            this.resendActivationEmail(user);
            throw ex;
        }
        authService.clearAuthAttempts(ip);
        String accessToken = jwtService.generateAccessToken(applicationUser.getUsername());
        String refreshToken = jwtService.generateRefreshToken(applicationUser.getUsername());
        // uncomment this after Ipv6 problem solved.
//        this.jwtService.associateAccessToken(accessToken, this.utilService.getClientIp(request));
        return new AuthResponse(refreshToken, accessToken);
    }

    @PostMapping("/signup")
    public SignUpResponse signUp(@Valid @RequestBody SignUpUser signUpUser,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) {
        String ip = this.utilService.getClientIp(request);
        authService.clearAuthAttempts(ip);
        if (!authService.isIpNonBlocked(ip)) {
            throw new IpBlockedException();
        }
        if (bindingResult.hasErrors()) {
            throw new SignUpValidationFailedException("Invalid Sign up user data");
        }
        try {
            User user = accountService.createNewUser(signUpUser);
            this.emailService.sendUserEmailVerificationCode(user);
            return new SignUpResponse(true, null);
        } catch (UsernameAlreadyExistException | EmailAlreadyExistException ex) {
            return new SignUpResponse(false, ex.getMessage());
        }
    }

    @GetMapping("check")
    public ActionStatusResponse isUsernameValid(@RequestParam(value = "username", required = false) String username,
                                                @RequestParam(value = "email", required = false) String email) {
        if (!UtilService.isNullOrEmpty(username) && !UtilService.isNullOrEmpty(email)) {
            return new ActionStatusResponse(false);
        }
        if (UtilService.isNullOrEmpty(username) && UtilService.isNullOrEmpty(email)) {
            return new ActionStatusResponse(false);
        }
        if (!UtilService.isNullOrEmpty(username)) {
            if (accountService.isUsernameExist(username)) {
                return new ActionStatusResponse(false);
            }
            return new ActionStatusResponse(true);
        }
        if (accountService.isEmailExist(email)) {
            return new ActionStatusResponse(false);
        }
        return new ActionStatusResponse(true);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @PostMapping("resendActivationEmail")
    public ActionStatusResponse resendActivationEmail(@RequestBody String email) {
        User user = this.accountService.findUserByEmail(email);
        return this.resendActivationEmail(user);
    }

    @PostMapping("activateAccount")
    public ActionStatusResponse emailVerify(@RequestBody AccountEmailVerifyObj obj, HttpServletRequest request) {
        String ip = this.utilService.getClientIp(request);
        if (!authService.isIpNonBlocked(ip)) {
            throw new IpBlockedException();
        }
        User user = this.accountService.findUserByUsername(obj.getUsername());
        if (user == null) {
            return new ActionStatusResponse(false);
        }
        VerificationToken verificationToken = this.verificationTokenService
                .getVerificationToken(user.getUsername());
        if (verificationToken == null) {
            return new ActionStatusResponse(false);
        }

        if (verificationToken.getAttempts() > 10) {
            this.authService.lockIpAddress(ip);
            throw new IpBlockedException();
        }

        //for each verification token, client has 3 attempts.
        if (verificationToken.getAttempts() > 3) {
            verificationToken.setAttempts(verificationToken.getAttempts() + 1);
            this.verificationTokenService.saveToken(user.getUsername(), verificationToken);
            throw new TooManyVerifyAttemptsException();
        }

        if (obj.getCode().equals(verificationToken.getCode())) {
            this.verificationTokenService.VerifiedEmail(user.getUsername());
            this.accountService.enableAccount(user.getUsername());
            return new ActionStatusResponse(true);
        }
        verificationToken.setAttempts(verificationToken.getAttempts() + 1);
        this.verificationTokenService.saveToken(user.getUsername(), verificationToken);
        return new ActionStatusResponse(false);
    }

    @Data
    private static class RefreshToken {
        private String refreshToken;
    }

    private ActionStatusResponse resendActivationEmail(User user) {
        this.emailService.sendUserEmailVerificationCode(user);
        return new ActionStatusResponse(true);
    }

}
