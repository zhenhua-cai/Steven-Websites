package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.Article;

public interface ArticleSaveExtension {
    void saveArticle(Article article);
}
