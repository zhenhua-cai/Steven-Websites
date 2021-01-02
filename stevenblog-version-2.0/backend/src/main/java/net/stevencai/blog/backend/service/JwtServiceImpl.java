package net.stevencai.blog.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.stevencai.blog.backend.repository.AuthTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final long JWT_ACCESS_TOKEN_EXPIRES_IN;
    private final long JWT_TOKEN_REFRESH_EXPIRES_IN;
    private UtilService utilService;
    private AuthTokensRepository authTokensRepository;

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public JwtServiceImpl(@Value("${jwt.token.access.expires.in}") long JWT_ACCESS_TOKEN_EXPIRES_IN,
                          @Value("${jwt.token.refresh.expires.in}") long JWT_TOKEN_REFRESH_EXPIRES_IN) {
        this.JWT_ACCESS_TOKEN_EXPIRES_IN = JWT_ACCESS_TOKEN_EXPIRES_IN;
        this.JWT_TOKEN_REFRESH_EXPIRES_IN = JWT_TOKEN_REFRESH_EXPIRES_IN;
    }

    @Autowired
    public void setAuthTokensRepository(AuthTokensRepository authTokensRepository) {
        this.authTokensRepository = authTokensRepository;
    }

    /**
     * extract username from jwt.
     * if jwt is expired, throws exception
     *
     * @param jwtToken jwt
     * @return username string
     */
    public String getUsernameFromJwt(String jwtToken) {
        return getClaimFromJwt(jwtToken, Claims::getSubject);
    }

    /**
     * extract expiration date from jwt
     *
     * @param jwtToken jwt
     * @return expiration date
     */
    public Date getExpirationDateFromJwt(String jwtToken) {
        return getClaimFromJwt(jwtToken, Claims::getExpiration);
    }

    /**
     * generate jwt for user.
     *
     * @param userDetails user detail which contains username.
     * @return jwt
     */
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(userDetails.getUsername());
    }

    /**
     * generate jwt for user.
     *
     * @param username username
     * @return jwt
     */
    @Override
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, JWT_ACCESS_TOKEN_EXPIRES_IN);
    }

    @Override
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, username, JWT_TOKEN_REFRESH_EXPIRES_IN);
    }

    @Override
    public boolean isAccessTokenIpChanged(String accessToken, String ip) {
        String associatedIp = this.authTokensRepository.getAccessTokenIp(accessToken);
        if (associatedIp == null) {
            return false;
        }
        return !ip.equals(associatedIp);
    }

    @Override
    public void associateAccessToken(String accessToken, String ip) {
        this.authTokensRepository.storeAccessToken(ip, accessToken);
    }

    @Override
    public boolean isRefreshTokenBlocked(String username, String refreshToken) {
        return this.authTokensRepository.isRefreshTokenBlocked(username, refreshToken);
    }

    @Override
    public void blockDeprecatedRefreshToken(String username, String refreshToken) {
        this.authTokensRepository.blockRefreshToken(username, refreshToken);
    }

    /**
     * validate jwt, check if username matches and if token expired.
     *
     * @param token       jwt
     * @param userDetails user details which contains username.
     * @return return true if valid, false otherwise.
     */
    public Boolean usernameMatch(String token, UserDetails userDetails) {
        if (userDetails == null) {
            return false;
        }
        Claims claims = getAllClaimsFromJwt(token);
        final String username = claims.getSubject();
        return username.equals(userDetails.getUsername());
    }

    /**
     * helper function to generate jwt.
     *
     * @param claims   key value pairs which will be used to create jwt:
     *                 subject - username
     *                 issuedAt - creation date time
     *                 expiration - jwt expiration date time
     *                 signWith - algorithm
     * @param username username
     * @return generated jwt
     */
    private String generateToken(Map<String, Object> claims, String username, long expireIn) {
        return Jwts.builder().setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireIn * 1000))
                .signWith(SignatureAlgorithm.HS512, utilService.getJwtSecret())
                .compact();
    }

    /**
     * helper function to get information from jwt.
     * if jwt is expired, return null.
     *
     * @param token          jwt
     * @param claimsFunction function will be apply to get info
     * @param <T>            claim info type.
     * @return claim info
     */
    private <T> T getClaimFromJwt(String token, Function<Claims, T> claimsFunction) {
        final Claims claims = getAllClaimsFromJwt(token);
        return claimsFunction.apply(claims);
    }

    /**
     * helper function to get all claims from jwt
     *
     * @param token jwt
     * @return all claims
     */
    private Claims getAllClaimsFromJwt(String token) {
        return Jwts.parser().setSigningKey(utilService.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    /**
     * helper function to check if jwt expired.
     *
     * @param token jwt
     * @return true if expired, otherwise false.
     */
    private boolean isJwtTokenExpired(String token) {
        final Date expiration = getExpirationDateFromJwt(token);
        return expiration.before(new Date());
    }

}
