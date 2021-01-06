package net.stevencai.blog.backend.response;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.Roles;
import net.stevencai.blog.backend.entity.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthResponse {
    private String accessToken;

    private String refreshToken;
    private int[] roles;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthResponse(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles.stream().mapToInt(
                (role) -> ((Role)role).getId()
        ).toArray();
    }
}
