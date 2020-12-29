package net.stevencai.blog.backend.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime time;
    private int status;
    private String error;
    private String message;

    public ErrorResponse() {
        time = LocalDateTime.now();
    }
}
