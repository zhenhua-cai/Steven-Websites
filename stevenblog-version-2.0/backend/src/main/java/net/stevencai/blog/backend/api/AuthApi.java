package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ApplicationUser;
import net.stevencai.blog.backend.clientResource.Roles;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.exception.TooManyAuthAttemptsException;
import net.stevencai.blog.backend.response.AuthResponse;
import net.stevencai.blog.backend.service.AccountService;
import net.stevencai.blog.backend.service.AuthService;
import net.stevencai.blog.backend.service.EmailService;
import net.stevencai.blog.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthApi {

    private AuthenticationManager authenticationManager;
    private AccountService accountService;
    private JwtService jwtService;
    private AuthService authService;
    private EmailService emailService;

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

    @PostMapping("/login")
    public AuthResponse login(@RequestBody(required = false) ApplicationUser applicationUser,
                              HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!authService.isOkForNextAuthAttempt(ip)) {
            throw new TooManyAuthAttemptsException();
        }
        try {
            authenticate(applicationUser.getUsername(), applicationUser.getPassword());
        } catch (BadCredentialsException ex) {
            if (authService.needsToNotifyOwnerAfterIncrease(ip)) {
                this.emailService.sendUserAuthAlert(applicationUser.getUsername(), ip);
            }
            throw ex;
        }
        authService.clearAuthAttempts(ip);
        User user = accountService.findUserByUsername(applicationUser.getUsername());
        String jwt = jwtService.generateJwt(applicationUser.getUsername());
        AuthResponse authResponse = new AuthResponse(jwt);
        Roles roles = new Roles(user.getAuthorities());
        authResponse.setRoles(roles);
        return authResponse;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
