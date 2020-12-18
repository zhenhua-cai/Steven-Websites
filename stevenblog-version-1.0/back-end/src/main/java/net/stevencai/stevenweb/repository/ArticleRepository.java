package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,String>, ArticleManager{
    Article findArticleById(String id);
    List<Article> findArticleByCreateDateTimeAfterOrderByCreateDateTimeDesc(LocalDateTime date);
    List<Article> findTop6ByOrderByCreateDateTimeDesc();
    List<Article> findTop6ByOrderByLastModifiedDateTimeDesc();
    List<Article> findArticleByUserUsernameOrderByLastModifiedDateTimeDesc(String username);
    Page<Article> findAllByUserUsernameOrderByLastModifiedDateTimeDesc(String username, Pageable pageable);
    Page<Article> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(String username, String title, Pageable pageable);
}
