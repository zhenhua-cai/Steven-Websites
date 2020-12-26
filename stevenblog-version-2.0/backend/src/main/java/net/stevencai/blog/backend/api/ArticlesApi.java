package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticleResourceResponse;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.service.ArticlesService;
import net.stevencai.blog.backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin("http://localhost:4200")
public class ArticlesApi extends PostApi{
    private ArticlesService articlesService;

    @Autowired
    public void setArticlesService(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping
    public ArticleResourceResponse getArticles(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<Article> pageable = articlesService.findArticles(page, size);
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/search")
    public ArticleResourceResponse searchArticlesByAuthorOrTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Page<Article> pageable = null;
        if(UtilService.isNullOrEmpty(sortBy)){
            pageable = searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeDesc(author,title,page,size);
        }
        else{
            pageable = searchArticlesByAuthorOrTitleOrderBy(title, author,sortBy,sortOrder, page, size);
        }
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/{id}")
    public ArticleResource getArticle(@PathVariable("id") String id) {
        return articlesService.findArticleById(id);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderBy(String title, String author,
                                                               String sortBy, Integer sortOrder,
                                                               int page, int size) {
        Page<Article> pageable = null;
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

    private Page<Article> searchArticlesByAuthorOrTitleOrderByTitleDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByAuthorAndTitleOrderByTitle(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return articlesService.findArticlesByAuthorOrderByTitleDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByTitleOrderByTitleDesc(title, page, size);
        }
        return articlesService.findArticlesOrderByTitleDesc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByTitleAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByAuthorAndTitleOrderByTitleAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return articlesService.findArticlesByAuthorOrderByTitleAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByTitleOrderByTitleAsc(title, page, size);
        }
        return articlesService.findArticlesOrderByTitleAsc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByAuthorAndTitleOrderByCreateDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return articlesService.findArticlesByAuthorOrderByCreateDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByTitleOrderByCreateDateTimeDesc(title, page, size);
        }
        return articlesService.findArticlesOrderByCreateDateTimeDesc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByAuthorAndTitleOrderByCreateDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return articlesService.findArticlesByAuthorOrderByCreateDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByTitleOrderByCreateDateTimeAsc(title, page, size);
        }
        return articlesService.findArticlesOrderByCreateDateTimeAsc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return articlesService.findArticlesByAuthorOrderByLastModifiedDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByTitleOrderByLastModifiedDateTimeDesc(title, page, size);
        }
        return articlesService.findArticlesOrderByLastModifiedDateTimeDesc(page, size);
    }

    private Page<Article> searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByAuthorAndTitleOrderByLastModifiedDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return articlesService.findArticlesByAuthorOrderByLastModifiedDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return articlesService.findArticlesByTitleOrderByLastModifiedDateTimeAsc(title, page, size);
        }
        return articlesService.findArticlesOrderByLastModifiedDateTimeAsc(page, size);
    }
}
