package net.stevencai.blog.backend.service;


import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.entity.Article;
import org.springframework.data.domain.Page;

public interface ArticlesService {
    ArticleResource findArticleById(String id);
    Page<Article> findArticles(int page, int size);

    Page<Article> findArticlesByTitle(String title, int page, int size);
}
