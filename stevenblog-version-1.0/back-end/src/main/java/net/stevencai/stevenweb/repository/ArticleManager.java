package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.entity.ArticleDraft;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleManager {
    void saveArticle(Article article);
}
