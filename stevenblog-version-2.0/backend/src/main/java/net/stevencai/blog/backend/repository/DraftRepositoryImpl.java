package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.ArticleDraft;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

@Repository
@Transactional
public class DraftRepositoryImpl extends PostSaveExtension implements ArticleDraftRepositoryExtension {

    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    @CachePut(value="articleDraftCache", key="#result.id")
    public ArticleDraft saveArticleDraft(ArticleDraft articleDraft) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        savePost(articleDraft, entityManager.unwrap(Session.class),"article_draft");
        entityManager.getTransaction().commit();
        return articleDraft;
    }

    @Override
    @CacheEvict("articleDraftCache")
    public void deleteByIdIfExists(String id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Session session = entityManager.unwrap(Session.class);
        Query<?> query = session.createQuery("delete from ArticleDraft ad where ad.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
}
