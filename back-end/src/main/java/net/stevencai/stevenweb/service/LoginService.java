package net.stevencai.stevenweb.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.exception.ClientIpIsBlockedException;
import net.stevencai.stevenweb.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService implements UserDetailsService {
    private AccountRepository accountRepository;
    private final int MAX_ATTEMPT = 5;
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginService(){
        int BLOCK_TIME = 3;
        TimeUnit BLOCK_TIME_UNIT = TimeUnit.HOURS;
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_TIME, BLOCK_TIME_UNIT)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }
    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public void loginSucceeded(String key){
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key){
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            //do nothing
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }
    public int getAttempts(String key){
        try {
            return attemptsCache.get(key);
        } catch (ExecutionException e) {
            return 0;
        }
    }
    public int getRemainingAttempts(String key){
        int remaining = MAX_ATTEMPT - getAttempts(key);
        return Math.max(remaining, 0);
    }
    public boolean isBlocked(String key){
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    @Override
    public User loadUserByUsername(String username) {
        String ip = getClientIP();
        if(isBlocked(ip)){
            throw new ClientIpIsBlockedException("Client IP Address is blocked");
        }
        User user =accountRepository.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Cannot find this user");
        }
        return user;
    }

    @Autowired
    private String getClientIP(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if(attributes == null){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
