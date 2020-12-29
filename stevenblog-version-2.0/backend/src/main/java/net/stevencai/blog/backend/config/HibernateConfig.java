package net.stevencai.blog.backend.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class HibernateConfig {

    @Bean
    public DataSource dataSource(@Autowired C3P0Properties c3P0Properties) throws PropertyVetoException {
        ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setDriverClass(c3P0Properties.getDriverClass());
        pooledDataSource.setUser(c3P0Properties.getUsername());
        pooledDataSource.setPassword(c3P0Properties.getPassword());
        pooledDataSource.setJdbcUrl(c3P0Properties.getJdbcUrl());
        pooledDataSource.setMaxPoolSize(c3P0Properties.getMaxPoolSize());
        pooledDataSource.setMaxIdleTime(c3P0Properties.getMaxIdleTime());

        return pooledDataSource;
    }
}
