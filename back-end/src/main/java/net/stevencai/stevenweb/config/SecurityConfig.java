package net.stevencai.stevenweb.config;

import net.stevencai.stevenweb.service.AccountService;
import net.stevencai.stevenweb.service.BlogAuthenticationFailureHandler;
import net.stevencai.stevenweb.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security-config.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private Environment env;
    private LoginService loginService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder);
    }
    @Bean
    public BlogAuthenticationFailureHandler blogAuthenticationFailureHandler(){
        return new BlogAuthenticationFailureHandler();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/articles/").permitAll()
                .antMatchers("/articles/all").permitAll()
                .antMatchers("/articles/new").hasRole("WRITER")
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
                .failureHandler(blogAuthenticationFailureHandler())
                .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(getIntProperty("app.remember.token.valid.seconds"))
                .and()
                .logout()
                .logoutSuccessUrl("/account/login?logout")
                .permitAll();
    }

    private int getIntProperty(String property) {
        String propertyValue = env.getProperty(property);
        assert propertyValue != null;
        return Integer.parseInt(propertyValue);
    }
}
