package net.stevencai.blog.backend.entity;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.ArticleResource;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Data
public class Article implements Post {
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

    public Article() {
        createDateTime = LocalDateTime.now();
        lastModifiedDateTime = createDateTime;
    }

    public Article(String id, String path) {
        this.id = id;
        createDateTime = LocalDateTime.now();
        lastModifiedDateTime = createDateTime;
    }

    public Article(String id,
                   String title,
                   String summary,
                   LocalDateTime createDateTime,
                   LocalDateTime lastModifiedDateTime,
                   String path, User user) {
        this.id = id;
        this.title = title;
        this.createDateTime = createDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.path = path;
        this.user = user;
        this.summary = summary;
    }

    public void modified() {
        lastModifiedDateTime = LocalDateTime.now();
    }

    public boolean isModified() {
        return lastModifiedDateTime.isEqual(createDateTime);
    }

}
