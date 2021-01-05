package net.stevencai.blog.backend.entity;

import lombok.Data;

@Data
public class VerificationToken {
    private String code;

    private int attempts;

    public VerificationToken() {
    }

    public VerificationToken(String code) {
        this.code = code;
    }

    public VerificationToken(String code, int attempts) {
        this.code = code;
        this.attempts = attempts;
    }
}
