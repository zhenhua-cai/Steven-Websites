package net.stevencai.blog.backend.clientResource;

import lombok.Data;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.entity.Post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ArticleResource {

    private String id;

    @NotNull
    private String title;

    @NotNull
    @Size(min = 1)
    private String content;

    @NotNull
    @Size(min = 1)
    private String username;

    private LocalDateTime lastModified;

    private LocalDateTime createDate;

    public ArticleResource(){
        createDate = LocalDateTime.now();
    }

    public ArticleResource(Article article){
        this.id= article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
    }
    public ArticleResource(Post article) {
        this.id= article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
    }
}
