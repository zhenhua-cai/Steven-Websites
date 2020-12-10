package net.stevencai.stevenweb.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(
        basePackages = {"net.stevencai.stevenweb"},
        excludeFilters = {
                @ComponentScan.Filter(type= FilterType.ANNOTATION, value = {EnableWebMvc.class, Controller.class})
        }
)
@PropertySource("classpath:application-config.properties")
public class RootConfig {
    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource getDataSource(){
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try{
            dataSource.setDriverClass(env.getProperty("jdbc.driver"));

            dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
            dataSource.setUser(env.getProperty("jdbc.user"));
            dataSource.setPassword(env.getProperty("jdbc.password"));

            //set connection pool props
            dataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
            dataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
            dataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
            dataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));
        }
        catch (PropertyVetoException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean entityManagerFactory(){
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(getDataSource());
        sessionFactoryBean.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
        sessionFactoryBean.setHibernateProperties(getHibernateProperties());
        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
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
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //read environment property and convert to int
    private int getIntProperty(String propName) {
        String propVal=env.getProperty(propName);
        assert propVal != null;
        return  Integer.parseInt(propVal);
    }
    private Properties getHibernateProperties() {

        // set hibernate properties
        Properties props = new Properties();

        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));

        return props;
    }
}
