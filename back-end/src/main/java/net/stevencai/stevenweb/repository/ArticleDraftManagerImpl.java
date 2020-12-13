package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.ArticleDraft;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class ArticleDraftManagerImpl implements ArticleDraftManager {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void saveArticleDraft(ArticleDraft articleDraft) {
        Session session = sessionFactory.getCurrentSession();
        Query<ArticleDraft> query = session.createNativeQuery(
                "insert into ArticleDraft (id, title, path, createDateTime, lastModifiedDateTime, userId) " +
                "values (:id, :title, :path, :createDateTime, :lastModifiedDateTime, :userid) " +
                "ON DUPLICATE  KEY UPDATE  lastModifiedDateTime=VALUES(lastModifiedDateTime), title=VALUES(title)",
                ArticleDraft.class);
        query.setParameter("id", articleDraft.getId());
        query.setParameter("title", articleDraft.getTitle());
        query.setParameter("path", articleDraft.getPath());
        query.setParameter("createDateTime", articleDraft.getCreateDateTime());
        query.setParameter("lastModifiedDateTime", articleDraft.getLastModifiedDateTime());
        query.setParameter("userid", articleDraft.getUser().getId());
        query.executeUpdate();
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
