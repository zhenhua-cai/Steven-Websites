package net.stevencai.blog.backend.service;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.Post;
import net.stevencai.blog.backend.exception.ArticleNotAbleToWriteToDiskException;
import net.stevencai.blog.backend.exception.ArticleNotFoundException;
import net.stevencai.blog.backend.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ArticlesServiceImpl implements ArticlesService {
    private ArticleRepository articleRepository;
    private UtilService utilService;
    @Autowired
    public void setUtilService(UtilService utilService) {
        this.utilService = utilService;
    }

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
    public Page<Article> findArticles(int page, int size) {
        return articleRepository.findAllByOrderByLastModifiedDateTimeDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<Article> findArticlesByTitle(String title, int page, int size) {
        return articleRepository.findAllByTitleContainingOrderByLastModifiedDateTimeDesc(title,PageRequest.of(page, size));
    }

    private ArticleResource getArticleResource(Article article){
        ArticleResource articleResource = new ArticleResource(article);
        loadArticleFromDisk(article.getPath(), articleResource);
        return articleResource;
    }

    private void loadArticleFromDisk(String path, ArticleResource articleResource){
        try(BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)){
            String line = "";
            StringBuilder article = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                article.append(line).append("\n");
            }
            articleResource.setContent(article.toString());
        } catch (IOException e) {
            throw new ArticleNotFoundException(e);
        }
    }
    private String createDirectoryForArticle(ArticleResource articleResource){
        String pathStr = utilService.getArticlesBasePath();
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
