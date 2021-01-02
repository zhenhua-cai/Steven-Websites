package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.ArticleDraft;

public interface ArticleDraftSaveExtension {
    ArticleDraft saveArticleDraft(ArticleDraft articleDraft);
    void deleteByIdIfExists(String id);
}
