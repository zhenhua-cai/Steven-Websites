package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import net.stevencai.blog.backend.entity.Role;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
public class Roles implements Serializable {
    private int[] roles;

    public Roles() {
    }

    public Roles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles.stream().mapToInt(
                (role) -> ((Role)role).getId()
        ).toArray();
    }
}
