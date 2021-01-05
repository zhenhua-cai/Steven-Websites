package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryExtension {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

}
