package net.stevencai.blog.backend.clientResource;

import lombok.Data;

@Data
public class AccountEmailVerifyObj {
    private String username;
    private String code;
}
