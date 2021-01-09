package net.stevencai.blog.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@Table(name = "article_draft")
public class ArticleDraft implements Post, Serializable {
    @Id
    private String id;

    @Column
    private String title;

    @Column
    private String summary;

    @Column
    private LocalDateTime createDateTime;

    @Column
    private LocalDateTime lastModifiedDateTime;

    @Column
    private String path;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public ArticleDraft() {
        createDateTime = LocalDateTime.now(ZoneOffset.UTC);
        lastModifiedDateTime = createDateTime;
    }

    public ArticleDraft(String path) {
        createDateTime = LocalDateTime.now(ZoneOffset.UTC);
        lastModifiedDateTime = createDateTime;
        this.path = path;
    }

    public ArticleDraft(String path, User user) {
        createDateTime = LocalDateTime.now(ZoneOffset.UTC);
        lastModifiedDateTime = createDateTime;
        this.path = path;
        this.user = user;
    }

    public ArticleDraft(Article article) {
        this.id = article.getId();
        this.user = article.getUser();
        this.createDateTime = article.getCreateDateTime();
        this.lastModifiedDateTime = article.getLastModifiedDateTime();
        this.title = article.getTitle();
        this.path = article.getPath();
        this.summary = article.getSummary();
    }

    public ArticleDraft(String id, String title, String summary, LocalDateTime createDate, LocalDateTime lastModified, String path, User user) {
        this.id = id;
        this.title = title;
        this.createDateTime = createDate;
        this.lastModifiedDateTime = lastModified;
        this.path = path;
        this.user = user;
        this.summary = summary;
    }

    public void modified() {
        lastModifiedDateTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    public boolean isModified() {
        return lastModifiedDateTime.isEqual(createDateTime);
    }

}
