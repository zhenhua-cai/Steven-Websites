package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.entity.Post;
import net.stevencai.blog.backend.entity.User;
import net.stevencai.blog.backend.exception.ArticleNotAbleToWriteToDiskException;
import net.stevencai.blog.backend.exception.ArticleNotFoundException;
import net.stevencai.blog.backend.repository.ArticleRepository;
import net.stevencai.blog.backend.repository.DraftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class ArticlesServiceImpl implements ArticlesService, ArticleDraftService {
    private ArticleRepository articleRepository;
    private DraftRepository draftRepository;
    private UtilService utilService;
    private AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(ArticlesServiceImpl.class);

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    public void setDraftRepository(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    @Override
    @Cacheable("articleCache")
    public ArticleResource findArticleById(String id) {
        Article article = articleRepository.findArticleById(id);
        if (article == null) {
            throw new ArticleNotFoundException("Article is missing");
        }
        return getArticleResource(article);
    }

    @Override
    public Page<Article> findArticles(int page, int size) {
        return articleRepository.findAllByOrderByLastModifiedDateTimeDesc(PageRequest.of(page, size));
    }

    @Override
    @CachePut(value = "articleCache", key = "#result.id")
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    @CachePut(value = "articleCache", key = "#result.id")
    public ArticleResource saveArticle(ArticleResource articleResource) {
        articleResource.setLastModified(LocalDateTime.now(ZoneOffset.UTC));
        Article article = getArticle(articleResource);
        saveArticleToDisk(article.getPath(), articleResource);
        saveArticle(article);
        return articleResource;
    }

    @Override
    public Page<Article> findArticlesByAuthorAndTitleOrderByTitle(String author, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByTitleDesc(author, title, PageRequest.of(page, size));

    }

    @Override
    public Page<Article> findArticlesByAuthorAndTitleOrderByTitleAsc(String author, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByTitleAsc(author, title, PageRequest.of(page, size));

    }

    @Override
    public Page<Article> findArticlesByAuthorAndTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeAsc(author, title, PageRequest.of(page, size));

    }

    @Override
    public Page<Article> findArticlesByAuthorOrderByTitleDesc(String author, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByTitleDesc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitleOrderByTitleDesc(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByTitleDesc(title, PageRequest.of(page, size));

    }

    @Override
    public Page<Article> findArticlesOrderByTitleDesc(int page, int size) {
        return articleRepository.findAllByOrderByTitleDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesOrderByTitleAsc(int page, int size) {
        return articleRepository.findAllByOrderByTitleAsc(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByAuthorOrderByCreateDateTimeDesc(String author, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByCreateDateTimeDesc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitleOrderByCreateDateTimeDesc(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByCreateDateTimeDesc(title, PageRequest.of(page, size));

    }

    @Override
    public Page<Article> findArticlesOrderByCreateDateTimeDesc(int page, int size) {
        return articleRepository.findAllByOrderByCreateDateTimeDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByAuthorOrderByCreateDateTimeAsc(String author, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByCreateDateTimeAsc(author, PageRequest.of(page, size));
    }


    @Override
    public Page<Article> findArticlesOrderByCreateDateTimeAsc(int page, int size) {
        return articleRepository.findAllByOrderByCreateDateTimeAsc(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeAsc(author, title, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(author, title, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByAuthorOrderByLastModifiedDateTimeDesc(String author, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByLastModifiedDateTimeDesc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitleOrderByCreateDateTimeAsc(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByCreateDateTimeAsc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitleOrderByLastModifiedDateTimeDesc(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByLastModifiedDateTimeDesc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesOrderByLastModifiedDateTimeDesc(int page, int size) {
        return articleRepository.findAllByOrderByCreateDateTimeDesc(PageRequest.of(page, size));
    }


    @Override
    public Page<Article> findArticlesByAuthorOrderByLastModifiedDateTimeAsc(String author, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByLastModifiedDateTimeAsc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitleOrderByLastModifiedDateTimeAsc(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByLastModifiedDateTimeAsc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesOrderByLastModifiedDateTimeAsc(int page, int size) {
        return articleRepository.findAllByOrderByLastModifiedDateTimeAsc(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByAuthorAndTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeDesc(author, title, PageRequest.of(page, size));
    }

    @Override
    @CacheEvict(value = "articleCache")
    public void deleteArticleById(String id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            this.articleRepository.deleteById(id);
            removeArticleFromDisk(article.get());
        }
    }

    @Override
    @CachePut(value = "articleCache", key = "#result.id")
    public ArticleResource publishArticle(ArticleResource articleResource) {
        if (articleResource.getId() == null) {
            articleResource.setId(utilService.generateUUIDForArticle(articleResource.getUsername()));
            articleResource.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
        }
        this.saveArticle(articleResource);
        draftRepository.deleteByIdIfExists(articleResource.getId());
        return articleResource;
    }

    @Override
    @Cacheable(value = "articleCache", unless = "#result == null")
    public ArticleResource loadArticleToEdit(String id) {
        Optional<ArticleDraft> articleDraft = draftRepository.findById(id);
        return articleDraft.map(this::getArticleResource).orElseGet(() -> findArticleById(id));
    }

    @Override
    public Page<Article> findArticlesByAuthorOrderByTitleAsc(String author, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByTitleAsc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitleOrderByTitleAsc(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByTitleAsc(title, PageRequest.of(page, size));

    }

    @Override
    @Cacheable("articleDraftCache")
    public ArticleResource findArticleDraftById(String id) {
        Optional<ArticleDraft> article = draftRepository.findById(id);
        if (!article.isPresent()) {
            throw new ArticleNotFoundException("Article is missing");
        }
        return getArticleResource(article.get());
    }

    @Override
    public Page<ArticleDraft> findArticleDrafts(int page, int size) {
        return draftRepository.findAllByOrderByLastModifiedDateTimeDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByTitle(String author, String title, int page, int size) {
        return draftRepository.findAllByUserUsernameAndTitleContainingOrderByTitleDesc(author, title, PageRequest.of(page, size));

    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByTitleAsc(String author, String title, int page, int size) {
        return draftRepository.findAllByUserUsernameAndTitleContainingOrderByTitleAsc(author, title, PageRequest.of(page, size));

    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size) {
        return draftRepository.findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeAsc(author, title, PageRequest.of(page, size));

    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorOrderByTitleDesc(String author, int page, int size) {
        return draftRepository.findAllByUserUsernameOrderByTitleDesc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByTitleOrderByTitleDesc(String title, int page, int size) {
        return draftRepository.findAllByTitleContainingOrderByTitleDesc(title, PageRequest.of(page, size));

    }

    @Override
    public Page<ArticleDraft> findArticleDraftsOrderByTitleDesc(int page, int size) {
        return draftRepository.findAllByOrderByTitleDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsOrderByTitleAsc(int page, int size) {
        return draftRepository.findAllByOrderByTitleAsc(PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorOrderByCreateDateTimeDesc(String author, int page, int size) {
        return draftRepository.findAllByUserUsernameOrderByCreateDateTimeDesc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByTitleOrderByCreateDateTimeDesc(String title, int page, int size) {
        return draftRepository.findAllByTitleContainingOrderByCreateDateTimeDesc(title, PageRequest.of(page, size));

    }

    @Override
    public Page<ArticleDraft> findArticleDraftsOrderByCreateDateTimeDesc(int page, int size) {
        return draftRepository.findAllByOrderByCreateDateTimeDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorOrderByCreateDateTimeAsc(String author, int page, int size) {
        return draftRepository.findAllByUserUsernameOrderByCreateDateTimeAsc(author, PageRequest.of(page, size));
    }


    @Override
    public Page<ArticleDraft> findArticleDraftsOrderByCreateDateTimeAsc(int page, int size) {
        return draftRepository.findAllByOrderByCreateDateTimeAsc(PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size) {
        return draftRepository.findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeAsc(author, title, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size) {
        return draftRepository.findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(author, title, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorOrderByLastModifiedDateTimeDesc(String author, int page, int size) {
        return draftRepository.findAllByUserUsernameOrderByLastModifiedDateTimeDesc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByTitleOrderByCreateDateTimeAsc(String title, int page, int size) {
        return draftRepository.findAllByTitleContainingOrderByCreateDateTimeAsc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByTitleOrderByLastModifiedDateTimeDesc(String title, int page, int size) {
        return draftRepository.findAllByTitleContainingOrderByLastModifiedDateTimeDesc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsOrderByLastModifiedDateTimeDesc(int page, int size) {
        return draftRepository.findAllByOrderByCreateDateTimeDesc(PageRequest.of(page, size));
    }


    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorOrderByLastModifiedDateTimeAsc(String author, int page, int size) {
        return draftRepository.findAllByUserUsernameOrderByLastModifiedDateTimeAsc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByTitleOrderByLastModifiedDateTimeAsc(String title, int page, int size) {
        return draftRepository.findAllByTitleContainingOrderByLastModifiedDateTimeAsc(title, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsOrderByLastModifiedDateTimeAsc(int page, int size) {
        return draftRepository.findAllByOrderByLastModifiedDateTimeAsc(PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size) {
        return draftRepository.findAllByUserUsernameAndTitleContainingOrderByCreateDateTimeDesc(author, title, PageRequest.of(page, size));
    }

    @Override
    @CachePut(value = "articleDraftCache", key = "#result.id")
    public ArticleResource saveArticleDraft(ArticleResource articleResource) {
        if (articleResource.getId() == null) {
            articleResource.setId(utilService.generateUUIDForArticle(articleResource.getUsername()));
            articleResource.setCreateDate(LocalDateTime.now(ZoneOffset.UTC));
        }
        articleResource.setLastModified(LocalDateTime.now(ZoneOffset.UTC));
        ArticleDraft articleDraft = getArticleDraft(articleResource);
        saveArticleToDisk(articleDraft.getPath(), articleResource);
        draftRepository.saveArticleDraft(articleDraft);
        return articleResource;
    }

    @Override
    @CacheEvict("articleDraftCache")
    public void deleteArticleDraftById(String id) {
        Optional<ArticleDraft> articleDraftOptional = draftRepository.findById(id);
        if (articleDraftOptional.isPresent()) {
            draftRepository.deleteById(id);
            removeArticleFromDisk(articleDraftOptional.get());
        }
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByAuthorOrderByTitleAsc(String author, int page, int size) {
        return draftRepository.findAllByUserUsernameOrderByTitleAsc(author, PageRequest.of(page, size));
    }

    @Override
    public Page<ArticleDraft> findArticleDraftsByTitleOrderByTitleAsc(String title, int page, int size) {
        return draftRepository.findAllByTitleContainingOrderByTitleAsc(title, PageRequest.of(page, size));

    }

    @Override
    public Page<Article> searchArticlesByAuthorOrTitleOrderBy(String author, String title,
                                                              String sortBy, Integer sortOrder,
                                                              int page, int size) {
        Page<Article> pageable;
        switch (sortBy.toLowerCase()) {
            case "title":
                if (sortOrder == null || sortOrder == -1) {
                    pageable = searchArticlesByAuthorOrTitleOrderByTitleDesc(author, title, page, size);
                } else {
                    pageable = searchArticlesByAuthorOrTitleOrderByTitleAsc(author, title, page, size);
                }
                break;
            case "createdatetime":
                if (sortOrder == null || sortOrder == -1) {
                    pageable = searchArticlesByAuthorOrTitleOrderByCreateDateTimeDesc(author, title, page, size);
                } else {
                    pageable = searchArticlesByAuthorOrTitleOrderByCreateDateTimeAsc(author, title, page, size);
                }
                break;
            case "lastmodifieddatetime":
            default:
                if (sortOrder == null || sortOrder == -1) {
                    pageable = searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
                } else {
                    pageable = searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeAsc(author, title, page, size);
                }
                break;
        }
        return pageable;
    }

    @Override
    public Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderBy(String author, String title,
                                                                        String sortBy, Integer sortOrder,
                                                                        int page, int size) {
        Page<ArticleDraft> pageable;
        switch (sortBy.toLowerCase()) {
            case "title":
                if (sortOrder == null || sortOrder == -1) {
                    pageable = searchArticleDraftsByAuthorOrTitleOrderByTitleDesc(author, title, page, size);
                } else {
                    pageable = searchArticleDraftsByAuthorOrTitleOrderByTitleAsc(author, title, page, size);
                }
                break;
            case "createdatetime":
                if (sortOrder == null || sortOrder == -1) {
                    pageable = searchArticleDraftsByAuthorOrTitleOrderByCreateDateTimeDesc(author, title, page, size);
                } else {
                    pageable = searchArticleDraftsByAuthorOrTitleOrderByCreateDateTimeAsc(author, title, page, size);
                }
                break;
            case "lastmodifieddatetime":
            default:
                if (sortOrder == null || sortOrder == -1) {
                    pageable = searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
                } else {
                    pageable = searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeAsc(author, title, page, size);
                }
                break;
        }
        return pageable;
    }

    @Override
    public Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticleDraftsByAuthorOrderByLastModifiedDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByTitleOrderByLastModifiedDateTimeDesc(title, page, size);
        }
        return findArticleDraftsOrderByLastModifiedDateTimeDesc(page, size);
    }

    @Override
    public Page<Article> searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticlesByAuthorOrderByLastModifiedDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticlesByTitleOrderByLastModifiedDateTimeDesc(title, page, size);
        }
        return findArticlesOrderByLastModifiedDateTimeDesc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByTitleDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticlesByAuthorAndTitleOrderByTitle(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticlesByAuthorOrderByTitleDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticlesByTitleOrderByTitleDesc(title, page, size);
        }
        return findArticlesOrderByTitleDesc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByTitleAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticlesByAuthorAndTitleOrderByTitleAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticlesByAuthorOrderByTitleAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticlesByTitleOrderByTitleAsc(title, page, size);
        }
        return findArticlesOrderByTitleAsc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticlesByAuthorAndTitleOrderByCreateDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticlesByAuthorOrderByCreateDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticlesByTitleOrderByCreateDateTimeDesc(title, page, size);
        }
        return findArticlesOrderByCreateDateTimeDesc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticlesByAuthorAndTitleOrderByCreateDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticlesByAuthorOrderByCreateDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticlesByTitleOrderByCreateDateTimeAsc(title, page, size);
        }
        return findArticlesOrderByCreateDateTimeAsc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticlesByAuthorOrderByLastModifiedDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticlesByTitleOrderByLastModifiedDateTimeAsc(title, page, size);
        }
        return findArticlesOrderByLastModifiedDateTimeAsc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByTitleDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByAuthorAndTitleOrderByTitle(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticleDraftsByAuthorOrderByTitleDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByTitleOrderByTitleDesc(title, page, size);
        }
        return findArticleDraftsOrderByTitleDesc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByTitleAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByAuthorAndTitleOrderByTitleAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticleDraftsByAuthorOrderByTitleAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByTitleOrderByTitleAsc(title, page, size);
        }
        return findArticleDraftsOrderByTitleAsc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticleDraftsByAuthorOrderByCreateDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByTitleOrderByCreateDateTimeDesc(title, page, size);
        }
        return findArticleDraftsOrderByCreateDateTimeDesc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticleDraftsByAuthorOrderByCreateDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByTitleOrderByCreateDateTimeAsc(title, page, size);
        }
        return findArticleDraftsOrderByCreateDateTimeAsc(page, size);
    }


    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return findArticleDraftsByAuthorOrderByLastModifiedDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return findArticleDraftsByTitleOrderByLastModifiedDateTimeAsc(title, page, size);
        }
        return findArticleDraftsOrderByLastModifiedDateTimeAsc(page, size);
    }

    private ArticleResource getArticleResource(Post post) {
        ArticleResource articleResource = new ArticleResource(post);
        loadArticleFromDisk(post.getPath(), articleResource);
        return articleResource;
    }

    private void loadArticleFromDisk(String path, ArticleResource articleResource) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line;
            StringBuilder article = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                article.append(line).append("\n");
            }
            articleResource.setContent(article.toString());
        } catch (IOException e) {
            logger.error("Article not found", e);
            throw new ArticleNotFoundException(e);
        }
    }

    private Article getArticle(ArticleResource articleResource) throws UsernameNotFoundException {
        User user = accountService.findUserByUsername(articleResource.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find username [" + articleResource.getUsername() + "] for post resource");
        }
        String parentPath = createDirectoryForArticle(articleResource);
        return new Article(articleResource.getId(),
                articleResource.getTitle(),
                articleResource.getSummary(),
                articleResource.getCreateDate(),
                articleResource.getLastModified(),
                articleResource.isPrivateMode(),
                createFileName(articleResource, parentPath), user);
    }

    private ArticleDraft getArticleDraft(ArticleResource articleResource) throws UsernameNotFoundException {
        User user = accountService.findUserByUsername(articleResource.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find username [" + articleResource.getUsername() + "] for post resource");
        }
        String parentPath = createDirectoryForDraft(articleResource);
        return new ArticleDraft(articleResource.getId(),
                articleResource.getTitle(),
                articleResource.getSummary(),
                articleResource.getCreateDate(),
                articleResource.getLastModified(),
                createFileName(articleResource, parentPath), user);
    }

    private void saveArticleToDisk(String path, ArticleResource articleResource) throws ArticleNotAbleToWriteToDiskException {
        try (BufferedWriter bUfferedWriter = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            bUfferedWriter.write(articleResource.getContent());

        } catch (IOException e) {
            logger.error("Unable to write to disk", e);
            throw new ArticleNotAbleToWriteToDiskException(e);
        }
    }

    private String createDirectoryForArticle(ArticleResource articleResource) {
        String pathStr = utilService.getArticlesBasePath();
        LocalDateTime dateTime = articleResource.getCreateDate();
        return createDirectoryForPost(pathStr, dateTime);
    }

    private String createDirectoryForDraft(ArticleResource articleResource) {
        String pathStr = utilService.getArticleDraftsBasePath();
        LocalDateTime dateTime = articleResource.getCreateDate();
        return createDirectoryForPost(pathStr, dateTime);
    }

    /**
     * create directory to save post. directory is constructed based on base path and current date.
     * format: basepath/year/month/day/
     *
     * @param basePath base path
     * @param dateTime current date time.
     * @return formatted directory path.
     */
    private String createDirectoryForPost(String basePath, LocalDateTime dateTime) {
        basePath += dateTime.getYear() + File.separator + dateTime.getMonth() + File.separator + dateTime.getDayOfMonth();
        Path path = Paths.get(basePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("Unable to create directory", e);
                throw new ArticleNotAbleToWriteToDiskException(e);
            }
        }
        return basePath;
    }

    private String createFileName(ArticleResource articleResource, String path) {
        return path + File.separator + articleResource.getId() + ".article";
    }

    private void removeArticleFromDisk(Post article) {
        Path path = Paths.get(article.getPath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("Article Not Found", e);
            throw new ArticleNotFoundException("Article doesn't exist");
        }
    }
}
