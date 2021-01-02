package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public User saveUser(User user) {
        return accountRepository.save(user);
    }

    @Override
    @Cacheable("userWithEmailCache")
    public User findUserByEmail(String email) {
        return accountRepository.findUserByEmail(email);
    }

    @Override
    @Cacheable("userWithUsernameCache")
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
}
