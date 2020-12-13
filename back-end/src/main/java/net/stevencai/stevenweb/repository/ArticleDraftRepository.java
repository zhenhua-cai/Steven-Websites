package net.stevencai.stevenweb.repository;

import net.stevencai.stevenweb.entity.ArticleDraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleDraftRepository extends JpaRepository<ArticleDraft, String>, ArticleDraftManager {

}
