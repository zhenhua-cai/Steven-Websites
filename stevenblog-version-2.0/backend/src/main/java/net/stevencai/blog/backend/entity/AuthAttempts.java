package net.stevencai.blog.backend.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AuthAttempts implements Serializable {
    private String ip;
    private int attempts;
    private LocalDateTime lastAttempt;

    public void setAttempts(int attempts) {
        this.attempts = attempts;
        this.lastAttempt = LocalDateTime.now();
    }
}
