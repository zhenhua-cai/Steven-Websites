package net.stevencai.stevenweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "net.stevencai.stevenweb.repository")
public class JpaConfig {
}
