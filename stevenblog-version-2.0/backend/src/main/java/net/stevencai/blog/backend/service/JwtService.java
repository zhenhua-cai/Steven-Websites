package net.stevencai.blog.backend.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    /**
     * extract username from jwt
     *
     * @param jwtToken jwt
     * @return username string
     */
    String getUsernameFromJwt(String jwtToken);

    /**
     * extract expiration date from jwt
     *
     * @param jwtToken jwt
     * @return expiration date
     */
    Date getExpirationDateFromJwt(String jwtToken);

    /**
     * validate jwt, check if username matches and if token expired.
     *
     * @param token       jwt
     * @param userDetails user details which contains username.
     * @return return true if valid, false otherwise.
     */
    Boolean usernameMatch(String token, UserDetails userDetails);

    /**
     * generate access token for user.
     *
     * @param userDetails user detail which contains username.
     * @return jwt
     */
    String generateAccessToken(UserDetails userDetails);

    /**
     * generate access token for user.
     *
     * @param username username
     * @return jwt
     */
    String generateAccessToken(String username);

    String generateRefreshToken(String username);

    boolean isAccessTokenIpChanged(String accessToken, String ip);

    void associateAccessToken(String accessToken, String ip);
    boolean isRefreshTokenBlocked(String username, String refreshToken);

    void blockDeprecatedRefreshToken(String username, String refreshToken);
}
