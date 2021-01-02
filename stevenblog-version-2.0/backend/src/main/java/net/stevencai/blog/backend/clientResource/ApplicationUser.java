package net.stevencai.blog.backend.clientResource;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationUser implements Serializable {
    private String username;
    private String password;
}
