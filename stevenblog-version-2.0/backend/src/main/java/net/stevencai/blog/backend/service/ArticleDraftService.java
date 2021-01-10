package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.entity.ArticleDraft;
import org.springframework.data.domain.Page;

public interface ArticleDraftService {
    ArticleResource findArticleDraftById(String id);
    Page<ArticleDraft> findArticleDrafts(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByTitle(String author, String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByTitleAsc(String author, String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorOrderByTitleDesc(String author, int page, int size);

    Page<ArticleDraft> findArticleDraftsByTitleOrderByTitleDesc(String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsOrderByTitleDesc(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorOrderByTitleAsc(String author, int page, int size);

    Page<ArticleDraft> findArticleDraftsByTitleOrderByTitleAsc(String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsOrderByTitleAsc(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorOrderByCreateDateTimeDesc(String author, int page, int size);

    Page<ArticleDraft> findArticleDraftsByTitleOrderByCreateDateTimeDesc(String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsOrderByCreateDateTimeDesc(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorOrderByCreateDateTimeAsc(String author, int page, int size);

    Page<ArticleDraft> findArticleDraftsByTitleOrderByCreateDateTimeAsc(String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsOrderByCreateDateTimeAsc(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorOrderByLastModifiedDateTimeDesc(String author, int page, int size);

    Page<ArticleDraft> findArticleDraftsByTitleOrderByLastModifiedDateTimeDesc(String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsOrderByLastModifiedDateTimeDesc(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorOrderByLastModifiedDateTimeAsc(String author, int page, int size);

    Page<ArticleDraft> findArticleDraftsByTitleOrderByLastModifiedDateTimeAsc(String title, int page, int size);

    Page<ArticleDraft> findArticleDraftsOrderByLastModifiedDateTimeAsc(int page, int size);

    Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size);

    ArticleResource saveArticleDraft(ArticleResource articleResource);

    void deleteArticleDraftById(String id);

    Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(String name, String title, int page, int size);

    Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderBy(String author, String title, String sortBy, Integer sortOrder, int page, int size);

    void updateArticleDraftTitle(ArticleResource articleResource);
}
