package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.entity.VerificationToken;
import net.stevencai.stevenweb.entity.VerificationTokenType;
import net.stevencai.stevenweb.frontendResource.UserResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.Locale;

public interface AccountService extends UserDetailsService {
    User saveUser(User user);
    User saveVerifiedUser(User user, VerificationToken token);
    User createUser(UserResource userResource);
    User findUserByEmail(String email);
    User loadUserByUsername(String username);
    User findUserByUsername(String username);
    VerificationToken createVerificationTokenForUser(User user, String token, VerificationTokenType type);
    VerificationToken findVerificationTokenByToken(String token);
    void updateUserPassword(User user, String password);
    void removeVerificationToken(VerificationToken token);

    void removeAllVerificationTokenForUser(User user);
    VerificationToken findVerificationTokenBefore(User user, LocalDateTime dateTime);
}
