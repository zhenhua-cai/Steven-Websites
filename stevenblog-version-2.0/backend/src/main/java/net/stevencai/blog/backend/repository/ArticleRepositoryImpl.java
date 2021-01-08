package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.Article;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

@Repository
@Transactional
public class ArticleRepositoryImpl extends PostSaveExtension implements ArticleRepositoryExtension {

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    @CachePut(value = "articleCache", key="#result.id")
    public Article saveArticle(Article article) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        savePost(article, entityManager.unwrap(Session.class), "article");
        entityManager.getTransaction().commit();
        return article;
    }
}
