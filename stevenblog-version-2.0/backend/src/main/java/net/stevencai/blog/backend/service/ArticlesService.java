package net.stevencai.blog.backend.service;


import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.entity.Article;
import org.springframework.data.domain.Page;

public interface ArticlesService {
    ArticleResource findArticleById(String id);
    Page<Article> findArticles(int page, int size);

    Page<Article> findArticlesByAuthorAndTitleOrderByTitle(String author, String title, int page, int size);

    Page<Article> findArticlesByAuthorAndTitleOrderByTitleAsc(String author, String title, int page, int size);

    Page<Article> findArticlesByAuthorAndTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size);

    Page<Article> findArticlesByAuthorOrderByTitleDesc(String author, int page, int size);

    Page<Article> findArticlesByTitleOrderByTitleDesc(String title, int page, int size);

    Page<Article> findArticlesOrderByTitleDesc(int page, int size);

    Page<Article> findArticlesByAuthorOrderByTitleAsc(String author, int page, int size);

    Page<Article> findArticlesByTitleOrderByTitleAsc(String title, int page, int size);

    Page<Article> findArticlesOrderByTitleAsc(int page, int size);

    Page<Article> findArticlesByAuthorOrderByCreateDateTimeDesc(String author, int page, int size);

    Page<Article> findArticlesByTitleOrderByCreateDateTimeDesc(String title, int page, int size);

    Page<Article> findArticlesOrderByCreateDateTimeDesc(int page, int size);

    Page<Article> findArticlesByAuthorOrderByCreateDateTimeAsc(String author, int page, int size);

    Page<Article> findArticlesByTitleOrderByCreateDateTimeAsc(String title, int page, int size);

    Page<Article> findArticlesOrderByCreateDateTimeAsc(int page, int size);

    Page<Article> findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size);

    Page<Article> findArticlesByAuthorOrderByLastModifiedDateTimeDesc(String author, int page, int size);

    Page<Article> findArticlesByTitleOrderByLastModifiedDateTimeDesc(String title, int page, int size);

    Page<Article> findArticlesOrderByLastModifiedDateTimeDesc(int page, int size);

    Page<Article> findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size);

    Page<Article> findArticlesByAuthorOrderByLastModifiedDateTimeAsc(String author, int page, int size);

    Page<Article> findArticlesByTitleOrderByLastModifiedDateTimeAsc(String title, int page, int size);

    Page<Article> findArticlesOrderByLastModifiedDateTimeAsc(int page, int size);

    Page<Article> findArticlesByAuthorAndTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size);
}
