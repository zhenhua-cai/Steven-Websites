package net.stevencai.blog.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Properties;

@Configuration
@PropertySource("classpath:email.properties")
@EnableAsync
@EnableCaching
public class AppConfig {
    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }


    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        return template;
    }

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("email.host"));
        mailSender.setPort(getIntProperty("email.port"));
        mailSender.setUsername(env.getProperty("email.username"));
        mailSender.setPassword(env.getProperty("email.password"));
        mailSender.setJavaMailProperties(getJavaMailSenderProperties());
        return mailSender;
    }
    private Properties getJavaMailSenderProperties() {

        // set hibernate properties
        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", env.getProperty("mail.transport.protocol"));
        props.setProperty("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        props.setProperty("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        props.setProperty("mail.debug", env.getProperty("mail.debug"));

        return props;
    }

    /**
     * read environment property and convert to int
     * @param propName
     * @return
     */
    private int getIntProperty(String propName) {
        String propVal=env.getProperty(propName);
        assert propVal != null;
        return  Integer.parseInt(propVal);
    }
}
