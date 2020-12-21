package net.stevencai.blog.backend.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Table(name="roles")
public class Role implements GrantedAuthority, Serializable {

    @Id
    private int id;

    @Column(name = "title")
    private String authority;

    public Role() {
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id &&
                authority.equals(role.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority);
    }
}
