package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.Article;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class ArticleRepositoryImpl extends PostManager implements ArticleManager {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void saveArticle(Article article) {
        savePost(article, sessionFactory);
    }

}
