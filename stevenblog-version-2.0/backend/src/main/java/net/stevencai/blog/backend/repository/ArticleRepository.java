package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, String> {
    Article findArticleById(String id);

    List<Article> findArticleByCreateDateTimeAfterOrderByCreateDateTimeDesc(LocalDateTime date);

    List<Article> findTop6ByOrderByCreateDateTimeDesc();

    List<Article> findTop6ByOrderByLastModifiedDateTimeDesc();

    List<Article> findArticleByUserUsernameOrderByLastModifiedDateTimeDesc(String username);

    Page<Article> findAllByUserUsernameOrderByLastModifiedDateTimeDesc(String username, Pageable pageable);

    Page<Article> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(String username, String title, Pageable pageable);

    Page<Article> findAllByOrderByLastModifiedDateTimeDesc(Pageable pageable);
    Page<Article> findArticlesByTitle(String title, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByLastModifiedDateTimeDesc(String title, Pageable pageable);


}
