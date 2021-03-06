package net.stevencai.blog.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "article")
@Data
public class Article implements Post, Serializable {
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

    @Column(name = "private")
    private boolean privateMode;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Article() {
        createDateTime = LocalDateTime.now(ZoneOffset.UTC);
        lastModifiedDateTime = createDateTime;
    }

    public Article(String id, String path) {
        this.id = id;
        createDateTime = LocalDateTime.now(ZoneOffset.UTC);
        lastModifiedDateTime = createDateTime;
        this.path = path;
    }

    public Article(String id,
                   String title,
                   String summary,
                   LocalDateTime createDateTime,
                   LocalDateTime lastModifiedDateTime,
                   boolean privateMode,
                   String path, User user) {
        this.id = id;
        this.title = title;
        this.createDateTime = createDateTime;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.path = path;
        this.user = user;
        this.privateMode = privateMode;
        this.summary = summary;
    }

    public void modified() {
        lastModifiedDateTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    public boolean isModified() {
        return lastModifiedDateTime.isEqual(createDateTime);
    }
}
