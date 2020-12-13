package net.stevencai.stevenweb.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Data
public class Article {
    @Id
    private String id;

    @Column
    private String title;

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

    public Article(String id, String path, User user) {
        this.id = id;
        createDateTime = LocalDateTime.now();
        lastModifiedDateTime = createDateTime;
        this.path = path;
        this.user = user;
    }


    public void modified() {
        lastModifiedDateTime = LocalDateTime.now();
    }

    public boolean isModified() {
        return lastModifiedDateTime.isEqual(createDateTime);
    }

}
