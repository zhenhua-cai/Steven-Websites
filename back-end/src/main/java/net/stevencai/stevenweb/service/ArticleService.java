package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.frontendResource.ArticleResource;

import java.util.List;

public interface ArticleService {

    Article saveArticle(Article article);
    ArticleResource saveArticle(ArticleResource articleResource);
    void deleteArticleDraft(ArticleResource articleResource);

    ArticleResource findArticleById(String id);

    List<ArticleResource> findRecentArticlesTitles();

    ArticleResource saveDraft(ArticleResource articleResource);

    void deleteArticleDraftById(String id);

    void deleteArticleDraftByIdIfExists(String id);
}
