package net.stevencai.blog.backend.repository;

public interface AuthTokensRepository {
    boolean blockRefreshToken(String username, String token);
    void storeAccessToken(String ip, String token);
    String getAccessTokenIp(String accessToken);
    boolean isRefreshTokenBlocked(String username, String refreshToken);
    void cleanExpiredRefreshToken();
}
