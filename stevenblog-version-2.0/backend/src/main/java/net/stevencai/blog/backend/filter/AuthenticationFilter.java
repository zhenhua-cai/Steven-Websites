package net.stevencai.blog.backend.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import net.stevencai.blog.backend.service.AccountService;
import net.stevencai.blog.backend.service.JwtService;
import net.stevencai.blog.backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private AccountService accountService;
    private JwtService jwtService;
    private UtilService utilService;

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // getClientIp sometimes returns IPv6, sometimes returns Ipv4. uncomment this part after solving that problem
//                // if client ip changed. don't authenticate this client.
//                // this will send 401 back to client, and client needs to
//                // send refresh token to server to get a new access token
//                if(this.jwtService.isAccessTokenIpChanged(jwtToken, this.utilService.getClientIp(httpServletRequest))){
//                    throw new IllegalArgumentException("Client IP Changed");
//                }
                username = jwtService.getUsernameFromJwt(jwtToken);
            } catch (IllegalArgumentException | ExpiredJwtException
                    | SignatureException | MalformedJwtException | NullPointerException ignore) {
            }
        }
        if (username != null && !this.accountService.isAuthenticated()) {
            UserDetails userDetails = accountService.findUserByUsername(username);
            if (!userDetails.isAccountNonLocked()) {
                throw new LockedException("Account was Locked due to suspicious behaviour");
            }
            if (jwtService.usernameMatch(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
