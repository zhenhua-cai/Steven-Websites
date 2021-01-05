package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.entity.VerificationToken;
import net.stevencai.blog.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public void setVerificationTokenRepository(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void saveNewToken(String username, String verificationCode) {
        this.verificationTokenRepository.saveToken(username, new VerificationToken(verificationCode));
    }

    @Override
    public VerificationToken getVerificationToken(String username) {
        return this.verificationTokenRepository.getToken(username);
    }

    @Override
    public void VerifiedEmail(String username) {
        this.verificationTokenRepository.cleanCode(username);
    }

    @Override
    public void saveToken(String username, VerificationToken verificationToken) {
        this.verificationTokenRepository.saveToken(username, verificationToken);
    }
}
