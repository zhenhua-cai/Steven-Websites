package net.stevencai.blog.backend.repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.stevencai.blog.backend.entity.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class VerificationTokenRepositoryImpl implements VerificationTokenRepository {
    private final LoadingCache<String, VerificationToken> userVerificationTokens;

    public VerificationTokenRepositoryImpl(@Value("${user.verification.lifetime}") int codeLifeTime) {
        userVerificationTokens = CacheBuilder.newBuilder()
                .expireAfterWrite(codeLifeTime, TimeUnit.MINUTES)
                .build(new CacheLoader<String, VerificationToken>() {
                    @Override
                    public VerificationToken load(String s) throws Exception {
                        return null;
                    }
                });
    }

    @Override
    public void saveToken(String username, VerificationToken token) {
        this.userVerificationTokens.put(username, token);
    }

    @Override
    public void cleanCode(String username) {
        this.userVerificationTokens.invalidate(username);
    }

    @Override
    public VerificationToken getToken(String username) {
        try {
            return this.userVerificationTokens.get(username);
        } catch (Exception e) {
            return null;
        }
    }

}
