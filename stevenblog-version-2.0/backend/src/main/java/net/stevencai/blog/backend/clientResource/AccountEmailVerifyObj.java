package net.stevencai.blog.backend.clientResource;

import lombok.Data;

@Data
public class AccountEmailVerifyObj {
    private String verifiedBy;
    private String code;
    private boolean useUsername;
}
