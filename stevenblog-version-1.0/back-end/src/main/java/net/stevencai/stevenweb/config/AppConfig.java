package net.stevencai.stevenweb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Import({
        RootConfig.class,
        AppWebConfig.class
})
@ComponentScan(
        basePackages = {"net.stevencai.stevenweb.config"},
        excludeFilters = {
                @ComponentScan.Filter(
                        type= FilterType.ANNOTATION,
                        value = Controller.class
                )
        }
)
public class AppConfig {
}
