package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,String> {
    Article findArticleById(String id);
    List<Article> findArticleByCreateDateTimeAfterOrderByCreateDateTimeDesc(LocalDateTime date);
}
