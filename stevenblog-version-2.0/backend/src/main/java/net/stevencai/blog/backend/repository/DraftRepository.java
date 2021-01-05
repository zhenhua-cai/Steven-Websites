package net.stevencai.blog.backend.repository;


import net.stevencai.blog.backend.entity.ArticleDraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepository extends JpaRepository<ArticleDraft, String>, ArticleDraftRepositoryExtension {
    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(String username, String title, Pageable pageable);

    Page<ArticleDraft> findAllByOrderByLastModifiedDateTimeDesc(Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByTitleDesc(String author, String title, Pageable of);

    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByTitleAsc(String author, String title, Pageable of);

    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeAsc(String author, String title, Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameOrderByTitleDesc(String user_username, Pageable pageable);

    Page<ArticleDraft> findAllByTitleContainingOrderByTitleDesc(String title, Pageable pageable);

    Page<ArticleDraft> findAllByOrderByTitleDesc(Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameOrderByTitleAsc(String user_username, Pageable pageable);

    Page<ArticleDraft> findAllByTitleContainingOrderByTitleAsc(String title, Pageable pageable);

    Page<ArticleDraft> findAllByOrderByTitleAsc(Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameOrderByCreateDateTimeDesc(String author, Pageable pageable);

    Page<ArticleDraft> findAllByTitleContainingOrderByCreateDateTimeDesc(String title, Pageable pageable);

    Page<ArticleDraft> findAllByOrderByCreateDateTimeDesc(Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameOrderByCreateDateTimeAsc(String author, Pageable pageable);

    Page<ArticleDraft> findAllByTitleContainingOrderByCreateDateTimeAsc(String title, Pageable pageable);

    Page<ArticleDraft> findAllByOrderByCreateDateTimeAsc(Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameOrderByLastModifiedDateTimeDesc(String author, Pageable pageable);

    Page<ArticleDraft> findAllByTitleContainingOrderByLastModifiedDateTimeDesc(String title, Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeAsc(String author, String title, Pageable pageable);

    Page<ArticleDraft> findAllByTitleContainingOrderByLastModifiedDateTimeAsc(String title, Pageable pageable);

    Page<ArticleDraft> findAllByOrderByLastModifiedDateTimeAsc(Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameOrderByLastModifiedDateTimeAsc(String author, Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeDesc(String author, String title, Pageable pageable);

}
