package net.stevencai.blog.backend.repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.stevencai.blog.backend.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Repository
public class AuthTokensRepositoryImpl implements AuthTokensRepository {
    private RedisTemplate<String, Object> redis;
    private final LoadingCache<String, String> accessTokenWithIp;
    private UtilService utilService;
    private final String REFRESH_TOKEN_OWNER_SET_NAME;
    private final int REFRESH_CACHE_IN;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public AuthTokensRepositoryImpl(@Value("${jwt.token.refresh.owner.set.name}") String refreshTokenOwnerSetName,
                                    @Value("${jwt.token.access.expires.in}") int REFRESH_CACHE_IN) {
        this.REFRESH_TOKEN_OWNER_SET_NAME = refreshTokenOwnerSetName;
        this.REFRESH_CACHE_IN = REFRESH_CACHE_IN + 5;
        accessTokenWithIp = CacheBuilder.newBuilder()
                .expireAfterWrite(REFRESH_CACHE_IN, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return "";
                    }
                });
    }

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public void setRedis(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    @Override
    public boolean blockRefreshToken(String username, String token) {
        SetOperations<String, Object> setOperations = redis.opsForSet();
        Boolean isMember = setOperations.isMember(username, token);
        if (isMember != null && isMember) {
            return false;
        }
        setOperations.add(username, token);
        setOperations.add(REFRESH_TOKEN_OWNER_SET_NAME, username);
        return true;
    }

    @Override
    public void storeAccessToken(String ip, String token) {
        this.accessTokenWithIp.put(token, ip);
    }

    @Override
    public String getAccessTokenIp(String accessToken) {
        try {
            return this.accessTokenWithIp.get(accessToken);
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override
    public boolean isRefreshTokenBlocked(String username, String refreshToken) {
        SetOperations<String, Object> setOperations = redis.opsForSet();
        Boolean isMember = setOperations.isMember(username, refreshToken);
        return isMember != null && isMember;
    }

    /**
     * cleaning expired refresh tokens.
     */
    @Async
    @Override
//    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanExpiredRefreshToken() {
        SetOperations<String, Object> setOperations = redis.opsForSet();
        Set<Object> owners = setOperations.members(REFRESH_TOKEN_OWNER_SET_NAME);
        if (owners == null) {
            return;
        }
        Date currentTime = new Date();
        logger.info("Started cleaning expired refresh token");
        for (Object owner : owners) {
            Set<Object> tokens = setOperations.members((String) owner);
            if (tokens == null) {
                continue;
            }
            if (tokens.size() == 0) {
                redis.delete((String)owner);
                setOperations.remove(REFRESH_TOKEN_OWNER_SET_NAME, owner);
                continue;
            }
            for (Object token : tokens) {
                try {
                    if (isJwtTokenExpired((String) token, currentTime)) {
                        setOperations.remove((String) owner, token);
                    }
                } catch (Exception ex) {
                    setOperations.remove((String) owner, token);
                }
            }
        }
        logger.info("Finished cleaning expired refresh token");
    }

    private boolean isJwtTokenExpired(String token, Date currentTime) {
        Claims claims = Jwts.parser().setSigningKey(utilService.getJwtSecret()).parseClaimsJws(token).getBody();
        final Date expiration = claims.getExpiration();
        return expiration.before(currentTime);
    }
}
