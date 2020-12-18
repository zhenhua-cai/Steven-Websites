package net.stevencai.stevenweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="articleCategories")
public class ArticleCategory {
    @Id
    private int id;

    @Column(name="category")
    private String value;
}
