package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.ArticleDraft;

public interface ArticleDraftRepositoryExtension {
    ArticleDraft saveArticleDraft(ArticleDraft articleDraft);
    void deleteByIdIfExists(String id);
}
