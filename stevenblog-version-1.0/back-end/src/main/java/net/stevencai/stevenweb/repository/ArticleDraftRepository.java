package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.ArticleDraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleDraftRepository extends JpaRepository<ArticleDraft, String>, ArticleDraftManager {

    Page<ArticleDraft> findAllByUserUsernameOrderByLastModifiedDateTimeDesc(String username, Pageable pageable);

    Page<ArticleDraft> findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(String username, String title, Pageable pageable);
}
