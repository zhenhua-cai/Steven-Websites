package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.repository.AccountRepository;
import net.stevencai.blog.backend.repository.AuthAttemptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private AccountRepository accountRepository;
    private AuthAttemptsRepository authAttemptsRepository;

    @Autowired
    public void setAuthAttemptsRepository(AuthAttemptsRepository authAttemptsRepository) {
        this.authAttemptsRepository = authAttemptsRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = accountRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't exist");
        }
        return user;
    }

    @Override
    public boolean isOkForNextAuthAttempt(String ip) {
        return authAttemptsRepository.okForNextAttempts(ip);
    }

    @Override
    public void increaseAuthAttempts(String ip) {
        authAttemptsRepository.increaseAttempts(ip);
    }

    @Override
    public void clearAuthAttempts(String remoteAddr) {
        authAttemptsRepository.clearAttempts(remoteAddr);
    }
}
