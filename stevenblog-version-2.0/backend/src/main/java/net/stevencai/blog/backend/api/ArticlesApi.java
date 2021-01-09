package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticlesListResponse;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.response.ActionStatusResponse;
import net.stevencai.blog.backend.response.ArticleResponse;
import net.stevencai.blog.backend.service.ArticlesService;
import net.stevencai.blog.backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/articles")
public class ArticlesApi extends PostApi {
    private ArticlesService articlesService;

    @Autowired
    public void setArticlesService(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping
    public ArticlesListResponse getArticles(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<Article> pageable = articlesService.findArticles(page, size);
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/search")
    public ArticlesListResponse searchArticlesByAuthorOrTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Page<Article> pageable;
        if (UtilService.isNullOrEmpty(sortBy)) {
            pageable = this.articlesService.searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else {
            pageable = this.articlesService.searchArticlesByAuthorOrTitleOrderBy(title, author, sortBy, sortOrder, page, size);
        }
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable("id") String id) {
        return new ArticleResponse(articlesService.findArticleById(id));
    }

    /**
     * this method will call loadArticleToEdit method.
     * which will first check if there's an article draft with the same id,
     * if there is, return the article draft. otherwise load article.
     *
     * @param id article id.
     * @return article response which contains article resource.
     */
    @GetMapping("/edit/{id}")
    public ArticleResponse loadArticleToEdit(@PathVariable("id") String id) {
        return new ArticleResponse(articlesService.loadArticleToEdit(id));
    }

    @DeleteMapping("/delete/{id}")
    public ActionStatusResponse deleteArticle(@PathVariable("id") String id) {
        if (id == null) {
            return new ActionStatusResponse(false);
        }
        articlesService.deleteArticleById(id);
        return new ActionStatusResponse(true);
    }

    @PostMapping("/publish")
    public ArticleResponse publishArticle(@RequestBody ArticleResource articleResource, Principal principal) {
        if (articleResource == null) {
            return new ArticleResponse();
        }
        if (articleResource.getUsername() == null) {
            articleResource.setUsername(principal.getName());
        }
        articleResource = articlesService.publishArticle(articleResource);
        return new ArticleResponse(articleResource);
    }


}
