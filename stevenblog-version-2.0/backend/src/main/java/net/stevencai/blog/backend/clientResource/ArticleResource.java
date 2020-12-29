package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.entity.Post;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ArticleResource {

    private String id;

    private String title;

    private String summary;

    private String content;

    private String username;

    private LocalDateTime lastModified;

    private LocalDateTime createDate;

    public ArticleResource() {
    }

    public ArticleResource(Article article) {
        this.id = article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
        this.summary = article.getSummary();
    }

    public ArticleResource(Post article) {
        this.id = article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
        this.summary = article.getSummary();
    }
}
