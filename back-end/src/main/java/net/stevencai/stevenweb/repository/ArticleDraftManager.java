package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.ArticleDraft;

public interface ArticleDraftManager {

    void saveArticleDraft(ArticleDraft articleDraft);
    void deleteByIdIfExists(String id);
}
