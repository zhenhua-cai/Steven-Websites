package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.Article;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ArticleRepositoryImpl extends PostSaveExtension implements ArticleSaveExtension {

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void saveArticle(Article article) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        savePost(article, entityManager.unwrap(Session.class));
        entityManager.getTransaction().commit();
    }
}
