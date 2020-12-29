package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ApplicationUser;
import net.stevencai.blog.backend.clientResource.Roles;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.response.AuthResponse;
import net.stevencai.blog.backend.service.AccountService;
import net.stevencai.blog.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:4200")
public class AuthApi {

    private AuthenticationManager authenticationManager;
    private AccountService accountService;
    private JwtService jwtService;

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
    public AuthResponse login(@RequestBody(required = false) ApplicationUser applicationUser) {

        authenticate(applicationUser.getUsername(), applicationUser.getPassword());
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
