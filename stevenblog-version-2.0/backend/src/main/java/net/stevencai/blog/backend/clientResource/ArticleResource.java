package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.Post;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArticleResource implements Serializable {

    private String id;

    private String title;

    private String summary;

    private String content;

    private String username;

    private LocalDateTime lastModified;

    private LocalDateTime createDate;

    private boolean privateMode;

    public ArticleResource() {
    }

    public ArticleResource(Article article) {
        this.id = article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.privateMode = article.isPrivateMode();
    }

    public ArticleResource(Post article) {
        this.id = article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.privateMode = article.isPrivateMode();
    }
}
