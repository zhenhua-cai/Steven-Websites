package net.stevencai.blog.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "c3p0")
@Data
public class C3P0Properties {
    private int minPoolSize;
    private int maxPoolSize;
    private int maxIdleTime;
    private String driverClass;
    private String password;
    private String username;
    private String jdbcUrl;
}
