package net.stevencai.stevenweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security-config.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private Environment env;
    private DataSource dataSource;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery(
                "select username, password, true from users where username=?"
        )
        .authoritiesByUsernameQuery(
                "select users.username, enabled from users"
                +" inner join authorities on users.id = authorities.userId"
                +" inner join roles on roles.id = authorities.roleId"
                +" where users.username=?"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/articles").permitAll()
                .antMatchers("/articles/all").permitAll()
                .antMatchers("/articles/{id}").permitAll()
                .antMatchers("/articles/**").hasRole("WRITER")
                .antMatchers("/profile/**").authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/home").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/account/login")
                    .loginProcessingUrl("/authenticateTheUser")
                    .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(getIntProperty("app.remember.token.valid.seconds"))
                .and()
                .logout()
                .logoutSuccessUrl("/account/login?logout")
                .permitAll();
    }

    private int getIntProperty(String property){
        String propertyValue = env.getProperty(property);
        assert propertyValue != null;
        return Integer.parseInt(propertyValue);
    }
}
