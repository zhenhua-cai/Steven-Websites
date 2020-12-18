package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    void deleteAllByUser(User user);
    VerificationToken findByUserAndCreateDateTimeAfter(User user, LocalDateTime dateTime);
}

