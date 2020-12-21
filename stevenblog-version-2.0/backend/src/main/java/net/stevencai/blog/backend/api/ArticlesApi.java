package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticleResourceResponse;
import net.stevencai.blog.backend.clientResource.ResponsePage;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.repository.ArticleRepository;
import net.stevencai.blog.backend.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin("http://localhost:4200")
public class ArticlesApi {
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
    public ArticleResourceResponse searchArticlesByTitle(
            @RequestParam("title") String title,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Page<Article> pageable = articlesService.findArticlesByTitle(title,page, size);
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/{id}")
    public ArticleResource getArticle(@PathVariable("id") String id){
        return articlesService.findArticleById(id);
    }

    private ArticleResourceResponse constructArticleResourceResponse(Page<Article> pageable){
        List<ArticleResource> articles = pageable
                .get()
                .map(ArticleResource::new)
                .collect(Collectors.toList());
        ResponsePage responsePage = new ResponsePage();
        responsePage.setNumber(pageable.getNumber());
        responsePage.setSize(pageable.getSize());
        responsePage.setTotalElements(pageable.getTotalElements());
        responsePage.setTotalPages(pageable.getTotalPages());
        return new ArticleResourceResponse(articles,responsePage);
    }
}
