package net.stevencai.blog.backend.response;

import lombok.Data;

@Data
public class SignUpResponse {
    private boolean success;
    private String message;

    public SignUpResponse() {
    }

    public SignUpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
