package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.clientResource.SignUpUser;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.exception.EmailAlreadyExistException;
import net.stevencai.blog.backend.exception.EmailNotFoundException;
import net.stevencai.blog.backend.exception.UsernameAlreadyExistException;
import net.stevencai.blog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAccountRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @CachePut(value = "userCache", key = "#result.username")
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Cacheable(value = "userCache", unless = "#result == null")
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
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
    @CacheEvict(value = "userCache", key = "#result.username")
    public User createNewUser(SignUpUser signUpUser) {
        if (isEmailExist(signUpUser.getEmail())) {
            throw new EmailAlreadyExistException("Email is in use");
        }
        if (isUsernameExist(signUpUser.getUsername())) {
            throw new UsernameAlreadyExistException("Username was already taken");
        }
        User user = buildUser(signUpUser);
        user.setCreateDateTime(LocalDateTime.now());
        return saveUser(user);
    }

    @Override
    @Cacheable("usernameExistsCache")
    public boolean isUsernameExist(String username) {
        return userRepository.findUserByUsername(username) != null;
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    @Override
    @CacheEvict(value = "userCache", key = "#username")
    public void lockAccount(String username) {
        this.userRepository.lockAccount(username);
    }

    @Override
    @CacheEvict(value = "userCache", key = "#username")
    public void unlockAccount(String username) {
        this.userRepository.unlockAccount(username);
    }

    @Override
    @CacheEvict(value = "userCache", key = "#username")
    public void enableAccount(String username) {
        this.userRepository.enableAccount(username);
    }

    @Override
    @CacheEvict(value = "userCache", key = "#result.username")
    public User updatePassword(String email, String password) {
        User user = this.userRepository.findUserByEmail(email);
        if (email == null) {
            throw new EmailNotFoundException();
        }
        user.setPassword(this.encryptPassword(password));
        return this.userRepository.save(user);
    }

    private User buildUser(SignUpUser signUpUser) {
        User user = new User();
        user.setUsername(signUpUser.getUsername());
        user.setPassword(encryptPassword(signUpUser.getPassword()));
        user.setEmail(signUpUser.getEmail());
        user.setEnabled(false);
        return user;
    }

    private String encryptPassword(String rawPw) {
        return passwordEncoder.encode(rawPw);
    }
}
