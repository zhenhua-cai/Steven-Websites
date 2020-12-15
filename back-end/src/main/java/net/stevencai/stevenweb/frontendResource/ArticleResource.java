package net.stevencai.stevenweb.frontendResource;

import lombok.Data;
import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.entity.ArticleDraft;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

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
    public ArticleResource(ArticleDraft article){
        this.id= article.getId();
        this.username = article.getUser().getUsername();
        this.createDate = article.getCreateDateTime();
        this.lastModified = article.getLastModifiedDateTime();
        this.title = article.getTitle();
    }
}
