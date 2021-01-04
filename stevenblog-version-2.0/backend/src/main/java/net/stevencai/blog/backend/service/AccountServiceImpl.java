package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.clientResource.SignUpUser;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.exception.EmailAlreadyExistException;
import net.stevencai.blog.backend.exception.UsernameAlreadyExistException;
import net.stevencai.blog.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @CachePut(value = "userCache", key = "#result.username")
    public User saveUser(User user) {
        return accountRepository.save(user);
    }

    @Override
    @Cacheable("userWithEmailCache")
    public User findUserByEmail(String email) {
        return accountRepository.findUserByEmail(email);
    }

    @Override
    @Cacheable("userCache")
    public User findUserByUsername(String username) {
        return accountRepository.findUserByUsername(username);
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public User createNewUser(SignUpUser signUpUser) {
        if (isEmailExist(signUpUser.getEmail())) {
            throw new EmailAlreadyExistException("Email is in use");
        }
        if (isUsernameExist(signUpUser.getUsername())) {
            throw new UsernameAlreadyExistException("Username was already taken");
        }
        User user = buildUser(signUpUser);
        return saveUser(user);
    }

    @Override
    @Cacheable("usernameExistsCache")
    public boolean isUsernameExist(String username) {
        return accountRepository.findUserByUsername(username) != null;
    }

    @Override
    @Cacheable("emailExistsCache")
    public boolean isEmailExist(String email) {
        return accountRepository.findUserByEmail(email) != null;
    }

    private User buildUser(SignUpUser signUpUser) {
        User user = new User();
        user.setUsername(signUpUser.getUsername());
        user.setPassword(encryptPassword(signUpUser.getPassword()));
        user.setEmail(signUpUser.getEmail());
        return user;
    }

    private String encryptPassword(String rawPw) {
        return passwordEncoder.encode(rawPw);
    }
}
