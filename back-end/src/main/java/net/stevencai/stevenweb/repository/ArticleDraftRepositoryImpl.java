package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.ArticleDraft;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class ArticleDraftRepositoryImpl extends  PostManager implements ArticleDraftManager {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void saveArticleDraft(ArticleDraft articleDraft) {
        savePost(articleDraft,sessionFactory);
    }
    @Transactional
    @Override
    public void deleteByIdIfExists(String id) {
        Session session = sessionFactory.getCurrentSession();
        Query<?> query = session.createQuery("delete from ArticleDraft ad where ad.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
