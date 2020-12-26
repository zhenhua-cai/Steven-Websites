package net.stevencai.blog.backend.response;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.Roles;

@Data
public class AuthResponse {
    private int httpStatus;
    private String jwt;
    private String msg;
    private Roles role;

    public AuthResponse() {
    }

    public AuthResponse(int status) {
        this.httpStatus = status;
    }

    public AuthResponse(int httpStatus, String jwt) {
        this.httpStatus = httpStatus;
        this.jwt = jwt;
    }


    public AuthResponse(int httpStatus, String jwt, String msg) {
        this.httpStatus = httpStatus;
        this.jwt = jwt;
        this.msg = msg;
    }

}
