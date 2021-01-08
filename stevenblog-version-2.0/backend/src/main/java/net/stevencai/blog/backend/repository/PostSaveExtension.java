package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.entity.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class PostSaveExtension {

    public void savePost(Post post, Session session, String tableName) {

        String queryStr = "insert into " + tableName + " (id, title, summary, path, createDateTime, lastModifiedDateTime, userId) " +
                "values (:id, :title,:summary, :path, :createDateTime, :lastModifiedDateTime, :userid) " +
                "ON DUPLICATE  KEY UPDATE  lastModifiedDateTime=VALUES(lastModifiedDateTime), title=VALUES(title)";
        System.out.println(queryStr);
        Query<ArticleDraft> query = session.createNativeQuery(queryStr, ArticleDraft.class);
        query.setParameter("id", post.getId());
        query.setParameter("title", post.getTitle());
        query.setParameter("summary", post.getSummary());
        query.setParameter("path", post.getPath());
        query.setParameter("createDateTime", post.getCreateDateTime());
        query.setParameter("lastModifiedDateTime", post.getLastModifiedDateTime());
        query.setParameter("userid", post.getUser().getId());
        query.executeUpdate();

    }
}
