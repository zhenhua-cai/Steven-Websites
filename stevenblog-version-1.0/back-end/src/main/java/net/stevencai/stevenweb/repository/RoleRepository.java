package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleById(int id);
}
