package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.ArticleDraft;
import net.stevencai.stevenweb.entity.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class PostManager {

    public void savePost(Post post, SessionFactory sessionFactory){
        Session session = sessionFactory.getCurrentSession();

        String queryStr = "insert into "+ post.getClass().getSimpleName()+" (id, title, path, createDateTime, lastModifiedDateTime, userId) " +
                "values (:id, :title, :path, :createDateTime, :lastModifiedDateTime, :userid) " +
                "ON DUPLICATE  KEY UPDATE  lastModifiedDateTime=VALUES(lastModifiedDateTime), title=VALUES(title)";
        System.out.println(queryStr);
        Query<ArticleDraft> query = session.createNativeQuery(queryStr, ArticleDraft.class);
        query.setParameter("id", post.getId());
        query.setParameter("title", post.getTitle());
        query.setParameter("path", post.getPath());
        query.setParameter("createDateTime", post.getCreateDateTime());
        query.setParameter("lastModifiedDateTime", post.getLastModifiedDateTime());
        query.setParameter("userid", post.getUser().getId());
        query.executeUpdate();
    }
}
