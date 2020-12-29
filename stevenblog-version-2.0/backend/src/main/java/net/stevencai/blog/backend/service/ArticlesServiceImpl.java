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
import net.stevencai.blog.backend.response.ArticleResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Service
public class ArticlesServiceImpl implements ArticlesService, ArticleDraftService {
    private ArticleRepository articleRepository;
    private DraftRepository draftRepository;
    private UtilService utilService;
    private AccountService accountService;

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
    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void saveArticle(ArticleResource articleResource) {
        articleResource.setLastModified(LocalDateTime.now());
        Article article = getArticle(articleResource);
        saveArticleToDisk(article.getPath(), articleResource);
        saveArticle(article);
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
    public void deleteArticleById(String id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent()) {
            this.articleRepository.deleteById(id);
            removeArticleFromDisk(article.get());
        }
    }

    @Override
    public ArticleResource publishArticle(ArticleResource articleResource) {
        if (articleResource.getId() == null) {
            articleResource.setId(utilService.generateUUIDForArticle(articleResource.getUsername()));
            articleResource.setCreateDate(LocalDateTime.now());
        }
        this.saveArticle(articleResource);
        draftRepository.deleteByIdIfExists(articleResource.getId());
        return articleResource;
    }

    @Override
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
    public ArticleResource saveArticleDraft(ArticleResource articleResource) {
        if (articleResource.getId() == null) {
            articleResource.setId(utilService.generateUUIDForArticle(articleResource.getUsername()));
            articleResource.setCreateDate(LocalDateTime.now());
        }
        articleResource.setLastModified(LocalDateTime.now());
        ArticleDraft articleDraft = getArticleDraft(articleResource);
        saveArticleToDisk(articleDraft.getPath(), articleResource);
        draftRepository.saveArticleDraft(articleDraft);
        return articleResource;
    }

    @Override
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

    private ArticleResource getArticleResource(Post post) {
        ArticleResource articleResource = new ArticleResource(post);
        loadArticleFromDisk(post.getPath(), articleResource);
        return articleResource;
    }

    private void loadArticleFromDisk(String path, ArticleResource articleResource) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            String line = "";
            StringBuilder article = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                article.append(line).append("\n");
            }
            articleResource.setContent(article.toString());
        } catch (IOException e) {
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
                createFileName(articleResource, parentPath), user);
    }

    private ArticleDraft getArticleDraft(ArticleResource articleResource) throws UsernameNotFoundException{
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
        try (BufferedWriter bUfferedWriter = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);) {
            bUfferedWriter.write(articleResource.getContent());

        } catch (IOException e) {
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
                throw new ArticleNotAbleToWriteToDiskException(e);
            }
        }
        return basePath;
    }

    private String createFileName(ArticleResource articleResource, String path) {
        return path + "/" + articleResource.getId() + ".article";
    }

    private void removeArticleFromDisk(Post article) {
        Path path = Paths.get(article.getPath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ArticleNotFoundException("Article doesn't exist");
        }
    }
}
