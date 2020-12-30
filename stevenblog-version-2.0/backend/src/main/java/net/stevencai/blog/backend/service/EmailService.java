package net.stevencai.blog.backend.service;

public interface EmailService {
    void sendUserAuthAlert(String username, String ip);
}
