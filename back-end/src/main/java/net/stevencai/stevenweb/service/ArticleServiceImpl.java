package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.entity.ArticleDraft;
import net.stevencai.stevenweb.entity.Post;
import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.exception.ArticleNotAbleToWriteToDiskException;
import net.stevencai.stevenweb.exception.ArticleNotFoundException;
import net.stevencai.stevenweb.frontendResource.ArticleResource;
import net.stevencai.stevenweb.repository.ArticleDraftRepository;
import net.stevencai.stevenweb.repository.ArticleRepository;
import net.stevencai.stevenweb.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ArticleRepository articleRepository;
    private ArticleDraftRepository articleDraftRepository;
    private AccountService accountService;
    private AppUtil appUtil;

    @Autowired
    public void setAppUtil(AppUtil appUtil) {
        this.appUtil = appUtil;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    public void setArticleDraftRepository(ArticleDraftRepository articleDraftRepository) {
        this.articleDraftRepository = articleDraftRepository;
    }

    @Secured("ROLE_ADMIN")
    @Override
    public void saveArticle(Article article) {
        articleRepository.saveArticle(article);
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ArticleResource saveArticle(ArticleResource articleResource) {
        Article article = getArticle(articleResource);
        article.setLastModifiedDateTime(LocalDateTime.now());
        saveArticleToDisk(article.getPath(), articleResource);
        saveArticle(article);
        return articleResource;
    }

    @Secured("ROLE_ADMIN")
    @Override
    public void deleteArticleDraft(ArticleResource articleResource) {
        articleDraftRepository.deleteById(articleResource.getId());
    }

    @Override
    public ArticleResource findArticleById(String id) {
        Article article = articleRepository.findArticleById(id);
        if(article == null){
            throw new ArticleNotFoundException("Article is missing");
        }
        return getArticleResource(article);
    }

    @Override
    public List<ArticleResource> findRecentArticlesTitles() {
        //find last 30 days articles
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        List<Article> recentArticles = articleRepository
                .findTop6ByOrderByLastModifiedDateTimeDesc();
        return recentArticles.stream().map(ArticleResource::new)
                .collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ArticleResource saveDraft(ArticleResource articleResource) {
        Article article = getArticle(articleResource);
        article.setLastModifiedDateTime(LocalDateTime.now());
        saveArticleToDisk(article.getPath(), articleResource);
        articleDraftRepository.saveArticleDraft(new ArticleDraft(article));
        return articleResource;
    }

    @Secured("ROLE_ADMIN")
    @Override
    public void deleteArticleDraftById(String id) {
        Optional<ArticleDraft> articleDraftOptional = articleDraftRepository.findById(id);
        if(articleDraftOptional.isPresent()){
            removeArticleFromDisk(articleDraftOptional.get());
            articleDraftRepository.deleteById(id);
        }
    }

    @Override
    public void deleteArticleDraftFromDBByIdIfExists(String id) {
        articleDraftRepository.deleteByIdIfExists(id);
    }

    @Override
    public Page<Article> findArticles(int page, int size) {
        return  articleRepository.findAll(
                PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"lastModifiedDateTime"))
        );
    }

    @Override
    public Page<Article> findArticlesByUsername(String username, int page, int size) {
        return articleRepository.findAllByUserUsernameOrderByLastModifiedDateTimeDesc(username,PageRequest.of(page,size));
    }

    @Override
    public Page<Article> findArticlesByUsernameAndTitle(String username, String title, int page, int size) {
        return articleRepository.findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(username,title,PageRequest.of(page,size));
    }

    @Override
    @Transactional
    public void deleteArticleById(String id) {
        Article article = articleRepository.findArticleById(id);
        if(article == null){
            return;
        }
        removeArticleFromDisk(article);
        articleRepository.delete(article);
    }

    @Override
    public Page<ArticleDraft> findDraftsByUsername(String username, int page, int size) {
        return articleDraftRepository.findAllByUserUsernameOrderByLastModifiedDateTimeDesc(username, PageRequest.of(page,size));
    }

    @Override
    public Page<ArticleDraft> findDraftsByUsernameAndTitle(String username, String title, int page, int size) {
        return articleDraftRepository.findAllByUserUsernameAndTitleContainingOrderByLastModifiedDateTimeDesc(username,title,PageRequest.of(page,size));

    }

    @Override
    public ArticleResource findDraftById(String id) {
        Optional<ArticleDraft> article = articleDraftRepository.findById(id);;
        if(!article.isPresent()){
            throw new ArticleNotFoundException("Article is missing");
        }
        return getArticleResource(article.get());
    }

    private Article getArticle(ArticleResource articleResource) throws UsernameNotFoundException {
        User user = accountService.findUserByUsername(articleResource.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find username [" + articleResource.getUsername() + "] for post resource");
        }
        String parentPath = createDirectoryForArticle(articleResource);
        Article article = new Article(articleResource.getId(),
                createFileName(articleResource,parentPath), user);
        article.setTitle(articleResource.getTitle());
        article.setLastModifiedDateTime(articleResource.getLastModified());
        return article;
    }

    private ArticleResource getArticleResource(Article article){
        ArticleResource articleResource = new ArticleResource(article);
        loadArticleFromDisk(article.getPath(), articleResource);
        return articleResource;
    }
    private ArticleResource getArticleResource(ArticleDraft article){
        ArticleResource articleResource = new ArticleResource(article);
        loadArticleFromDisk(article.getPath(), articleResource);
        return articleResource;
    }
    private void loadArticleFromDisk(String path, ArticleResource articleResource){
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path),StandardCharsets.UTF_8)){
            String line = "";
            StringBuilder article = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                article.append(line);
            }
            articleResource.setContent(article.toString());
        } catch (IOException e) {
            throw new ArticleNotFoundException(e);
        }
    }
    private void saveArticleToDisk(String path, ArticleResource articleResource) throws ArticleNotAbleToWriteToDiskException {
        try (BufferedWriter bUfferedWriter = Files.newBufferedWriter(Paths.get(path),StandardCharsets.UTF_8);) {
            bUfferedWriter.write(articleResource.getContent());

        } catch (IOException e) {
            throw new ArticleNotAbleToWriteToDiskException(e);
        }
    }
    private String createDirectoryForArticle(ArticleResource articleResource){
        String pathStr = appUtil.getArticlesBasePath();
        LocalDateTime dateTime = articleResource.getCreateDate();
        pathStr+=dateTime.getYear()+"/"+dateTime.getMonth()+"/"+dateTime.getDayOfMonth();
        Path path = Paths.get(pathStr);
        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new ArticleNotAbleToWriteToDiskException(e);
            }
        }
        return pathStr;
    }
    private String createFileName(ArticleResource articleResource, String path){
        return path+"/"+articleResource.getId()+".article";
    }
    private void removeArticleFromDisk(Post article)  {
        Path path = Paths.get(article.getPath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ArticleNotFoundException("Article doesn't exist");
        }
    }
}
