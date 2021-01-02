package net.stevencai.blog.backend.response;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.Roles;

@Data
public class AuthResponse {
    private String accessToken;

    private String refreshToken;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthResponse(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
