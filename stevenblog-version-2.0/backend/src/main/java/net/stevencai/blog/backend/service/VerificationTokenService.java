package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.VerificationToken;

public interface VerificationTokenService {
    void saveNewToken(String username, String verificationCode);

    VerificationToken getVerificationToken(String username);

    void VerifiedEmail(String username);

    void saveToken(String username, VerificationToken verificationToken);

}
