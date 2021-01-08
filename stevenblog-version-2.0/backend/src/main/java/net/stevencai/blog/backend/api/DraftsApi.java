package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticlesListResponse;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.response.ActionStatusResponse;
import net.stevencai.blog.backend.response.ArticleResponse;
import net.stevencai.blog.backend.service.ArticleDraftService;
import net.stevencai.blog.backend.service.UtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/api/drafts")
public class DraftsApi extends PostApi {
    private ArticleDraftService draftService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public void setdraftService(ArticleDraftService draftService) {
        this.draftService = draftService;
    }


    @GetMapping
    public ArticlesListResponse getArticleDrafts(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<ArticleDraft> pageable = draftService.findArticleDrafts(page, size);
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/search")
    public ArticlesListResponse searchArticleDraftsByAuthorOrTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Page<ArticleDraft> pageable = null;
        if (UtilService.isNullOrEmpty(sortBy)) {
            pageable = this.draftService.searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else {
            pageable = this.draftService.searchArticleDraftsByAuthorOrTitleOrderBy(title, author, sortBy, sortOrder, page, size);
        }
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticleDraft(@PathVariable("id") String id) {
        return new ArticleResponse(draftService.findArticleDraftById(id));
    }

    @PostMapping("save")
    public ArticleResponse saveArticleDraft(@RequestBody ArticleResource articleResource, Principal principal) {
        if(articleResource.getUsername() == null) {
            articleResource.setUsername(principal.getName());
        }
        return new ArticleResponse(draftService.saveArticleDraft(articleResource));
    }

    @DeleteMapping("delete/{id}")
    public ActionStatusResponse deleteArticleDraft(@PathVariable("id") String id) {
        if (id == null) {
            return new ActionStatusResponse(false);
        }
        draftService.deleteArticleDraftById(id);
        return new ActionStatusResponse(true);
    }
}
