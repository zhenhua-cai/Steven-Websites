package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
public class Roles implements Serializable {
    private String[] roles;

    public Roles() {
    }

    public Roles(String[] role) {
        this.roles = role;
    }
    public Roles(Collection<? extends GrantedAuthority> roles){
        this.roles= roles.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }
}
