package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.clientResource.SignUpUser;
import net.stevencai.blog.backend.entity.User;

public interface AccountService {
    User saveUser(User user);

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    boolean isAuthenticated();

    User createNewUser(SignUpUser signUpUser);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);

    void lockAccount(String username);

    void unlockAccount(String username);

    void enableAccount(String username);
}
