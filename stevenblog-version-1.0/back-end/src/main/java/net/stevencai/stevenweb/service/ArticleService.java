package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.entity.ArticleDraft;
import net.stevencai.stevenweb.frontendResource.ArticleResource;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {

    void saveArticle(Article article);
    ArticleResource saveArticle(ArticleResource articleResource);
    void deleteArticleDraft(ArticleResource articleResource);

    ArticleResource findArticleById(String id);

    List<ArticleResource> findRecentArticlesTitles();

    ArticleResource saveDraft(ArticleResource articleResource);

    void deleteArticleDraftById(String id);

    void deleteArticleDraftFromDBByIdIfExists(String id);

    Page<Article> findArticles(int page, int size);

    Page<Article> findArticlesByUsername(String name, int page, int size);

    Page<Article> findArticlesByUsernameAndTitle(String username, String title, int page, int size);

    void deleteArticleById(String id);

    Page<ArticleDraft> findDraftsByUsername(String name, int page, int size);

    Page<ArticleDraft> findDraftsByUsernameAndTitle(String name, String title, int page, int size);

    ArticleResource findDraftById(String id);
}
