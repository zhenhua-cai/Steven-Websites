package net.stevencai.stevenweb.service;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.entity.ArticleDraft;
import net.stevencai.stevenweb.entity.User;
import net.stevencai.stevenweb.exception.ArticleNotAbleToWriteToDiskException;
import net.stevencai.stevenweb.exception.ArticleNotFoundException;
import net.stevencai.stevenweb.frontendResource.ArticleResource;
import net.stevencai.stevenweb.repository.ArticleDraftRepository;
import net.stevencai.stevenweb.repository.ArticleRepository;
import net.stevencai.stevenweb.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
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
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ArticleResource saveArticle(ArticleResource articleResource) {
        Article article = getArticle(articleResource);
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
        LocalDateTime date = LocalDateTime.now().minusDays(30);
        List<Article> recentArticles = articleRepository.findArticleByCreateDateTimeAfterOrderByCreateDateTimeDesc(date);
        return recentArticles.stream().map(ArticleResource::new)
                .collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ArticleResource saveDraft(ArticleResource articleResource) {
        Article article = getArticle(articleResource);
        saveArticleToDisk(article.getPath(), articleResource);
        articleDraftRepository.save(new ArticleDraft(article));
        return articleResource;
    }

    @Secured("ROLE_ADMIN")
    @Override
    public void deleteArticleDraftById(String id) {
        articleDraftRepository.deleteById(id);
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
        return article;
    }

    private ArticleResource getArticleResource(Article article){
        ArticleResource articleResource = new ArticleResource(article);
        loadArticleFromDisk(article.getPath(), articleResource);
        return articleResource;
    }
    private void loadArticleFromDisk(String path, ArticleResource articleResource){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
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
        try (
                BufferedWriter bUfferedWriter =
                        new BufferedWriter(
                                new FileWriter(path))) {
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
        return path+"/"+articleResource.getId();
    }
}
