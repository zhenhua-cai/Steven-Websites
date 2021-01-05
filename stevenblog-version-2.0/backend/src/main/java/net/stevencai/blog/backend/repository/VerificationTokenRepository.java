package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.VerificationToken;

public interface VerificationTokenRepository {
    void saveToken(String username, VerificationToken token);

    void cleanCode(String username);

    VerificationToken getToken(String username);
}
