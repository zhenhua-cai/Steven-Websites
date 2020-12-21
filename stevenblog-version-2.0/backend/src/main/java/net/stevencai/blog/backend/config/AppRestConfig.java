package net.stevencai.blog.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import javax.persistence.EntityManager;

@Configuration
public class AppRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public AppRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
