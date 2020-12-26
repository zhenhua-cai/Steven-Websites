package net.stevencai.blog.backend.repository;

import net.stevencai.blog.backend.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
    Article findArticleById(String id);
    Page<Article> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(String username, String title, Pageable pageable);

    Page<Article> findAllByOrderByLastModifiedDateTimeDesc(Pageable pageable);

    Page<Article> findAllByUserUsernameAndTitleContainingOrderByTitleDesc(String author, String title, Pageable of);

    Page<Article> findAllByUserUsernameAndTitleContainingOrderByTitleAsc(String author, String title, Pageable of);

    Page<Article> findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeAsc(String author, String title, Pageable pageable);

    Page<Article> findAllByUserUsernameOrderByTitleDesc(String user_username, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByTitleDesc(String title, Pageable pageable);

    Page<Article> findAllByOrderByTitleDesc(Pageable pageable);

    Page<Article> findAllByUserUsernameOrderByTitleAsc(String user_username, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByTitleAsc(String title, Pageable pageable);

    Page<Article> findAllByOrderByTitleAsc(Pageable pageable);

    Page<Article> findAllByUserUsernameOrderByCreateDateTimeDesc(String author, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByCreateDateTimeDesc(String title, Pageable pageable);

    Page<Article> findAllByOrderByCreateDateTimeDesc(Pageable pageable);

    Page<Article> findAllByUserUsernameOrderByCreateDateTimeAsc(String author, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByCreateDateTimeAsc(String title, Pageable pageable);

    Page<Article> findAllByOrderByCreateDateTimeAsc(Pageable pageable);

    Page<Article> findAllByUserUsernameOrderByLastModifiedDateTimeDesc(String author, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByLastModifiedDateTimeDesc(String title, Pageable pageable);

    Page<Article> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeAsc(String author, String title, Pageable pageable);

    Page<Article> findAllByTitleContainingOrderByLastModifiedDateTimeAsc(String title, Pageable pageable);

    Page<Article> findAllByOrderByLastModifiedDateTimeAsc(Pageable pageable);

    Page<Article> findAllByUserUsernameOrderByLastModifiedDateTimeAsc(String author, Pageable pageable);

    Page<Article> findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeDesc(String author, String title, Pageable pageable);
}
