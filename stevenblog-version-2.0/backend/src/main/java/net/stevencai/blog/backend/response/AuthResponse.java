package net.stevencai.blog.backend.response;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.Roles;

@Data
public class AuthResponse {
    private String jwt;
    private Roles roles;

    public AuthResponse() {
    }

    public AuthResponse( String jwt) {
        this.jwt = jwt;
    }
}
