package net.stevencai.stevenweb.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="articleDraft")
public class ArticleDraft implements Post{
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
    @JoinColumn(name="userId")
    private User user;

    public ArticleDraft(){
        createDateTime = LocalDateTime.now();
        lastModifiedDateTime = createDateTime;
    }

    public ArticleDraft(String path){
        createDateTime = LocalDateTime.now();
        lastModifiedDateTime = createDateTime;
    }
    public ArticleDraft(String path, User user){
        createDateTime = LocalDateTime.now();
        lastModifiedDateTime = createDateTime;
        this.path = path;
        this.user = user;
    }

    public ArticleDraft(Article article){
        this.id = article.getId();
        this.user = article.getUser();
        this.createDateTime = article.getCreateDateTime();
        this.lastModifiedDateTime = article.getLastModifiedDateTime();
        this.title = article.getTitle();
        this.path = article.getPath();
    }

    public void modified(){
        lastModifiedDateTime = LocalDateTime.now();
    }

    public boolean isModified(){
        return lastModifiedDateTime.isEqual(createDateTime);
    }

}
