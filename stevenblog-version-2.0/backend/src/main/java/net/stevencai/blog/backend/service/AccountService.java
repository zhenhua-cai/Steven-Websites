package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.User;

public interface AccountService {
    User saveUser(User user);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
    boolean isAuthenticated();
}
