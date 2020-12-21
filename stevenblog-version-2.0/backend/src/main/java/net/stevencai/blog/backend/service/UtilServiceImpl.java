package net.stevencai.blog.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource({
        "classpath:articles.properties",
        "classpath:application.properties"
})
public class UtilServiceImpl implements UtilService {
    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Override
    public String getArticlesBasePath() {
        return env.getProperty("app.articles.base.path");
    }
}
