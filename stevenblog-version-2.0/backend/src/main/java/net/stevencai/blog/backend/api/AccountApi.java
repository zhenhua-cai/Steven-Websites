package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticlesListResponse;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.service.ArticleDraftService;
import net.stevencai.blog.backend.service.ArticlesService;
import net.stevencai.blog.backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/account")
public class AccountApi {
    private ArticlesService articlesService;
    private ArticleDraftService draftService;
    private PostApi postApi;


    @Autowired
    public void setDraftService(ArticleDraftService draftService) {
        this.draftService = draftService;
    }

    @Autowired
    public void setPostApi(PostApi postApi) {
        this.postApi = postApi;
    }

    @Autowired
    public void setArticlesService(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }


    @GetMapping("/articles/search")
    public ArticlesListResponse searchArticlesByAuthorOrTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            Principal principal) {
        Page<Article> pageable;
        if (UtilService.isNullOrEmpty(sortBy)) {
            pageable = this.articlesService.searchArticlesByAuthorOrTitleOrderByLastModifiedDateTimeDesc(principal.getName(), title, page, size);
        } else {
            pageable = this.articlesService.searchArticlesByAuthorOrTitleOrderBy(title, principal.getName(), sortBy, sortOrder, page, size);
        }
        return this.postApi.constructArticleResourceResponse(pageable);
    }

    @GetMapping("/drafts/search")
    public ArticlesListResponse searchArticleDraftsByAuthorOrTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            Principal principal) {
        Page<ArticleDraft> pageable;
        if (UtilService.isNullOrEmpty(sortBy)) {
            pageable = this.draftService.searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(principal.getName(), title, page, size);
        } else {
            pageable = this.draftService.searchArticleDraftsByAuthorOrTitleOrderBy(principal.getName(), title, sortBy, sortOrder, page, size);
        }
        return this.postApi.constructArticleResourceResponse(pageable);
    }
}
