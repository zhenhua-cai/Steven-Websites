package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticlesListResponse;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.response.ActionStatusResponse;
import net.stevencai.blog.backend.response.ArticleResponse;
import net.stevencai.blog.backend.service.ArticleDraftService;

import net.stevencai.blog.backend.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/drafts")
@CrossOrigin("http://localhost:4200")
public class DraftsApi extends PostApi {
    private ArticleDraftService draftService;

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
            pageable = searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else {
            pageable = searchArticleDraftsByAuthorOrTitleOrderBy(title, author, sortBy, sortOrder, page, size);
        }
        return constructArticleResourceResponse(pageable);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticleDraft(@PathVariable("id") String id) {
        return new ArticleResponse(draftService.findArticleDraftById(id));
    }

    @PostMapping("/save")
    public ArticleResponse saveArticleDraft(@RequestBody ArticleResource articleResource) {
        return new ArticleResponse(draftService.saveArticleDraft(articleResource));
    }

    @DeleteMapping("/delete/{id}")
    public ActionStatusResponse deleteArticleDraft(@PathVariable("id") String id) {
        if (id == null) {
            return new ActionStatusResponse(false);
        }
        draftService.deleteArticleDraftById(id);
        return new ActionStatusResponse(true);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderBy(String title, String author,
                                                                         String sortBy, Integer sortOrder,
                                                                         int page, int size) {
        Page<ArticleDraft> pageable = null;
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

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByTitleDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByAuthorAndTitleOrderByTitle(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return draftService.findArticleDraftsByAuthorOrderByTitleDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByTitleOrderByTitleDesc(title, page, size);
        }
        return draftService.findArticleDraftsOrderByTitleDesc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByTitleAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByAuthorAndTitleOrderByTitleAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return draftService.findArticleDraftsByAuthorOrderByTitleAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByTitleOrderByTitleAsc(title, page, size);
        }
        return draftService.findArticleDraftsOrderByTitleAsc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByCreateDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return draftService.findArticleDraftsByAuthorOrderByCreateDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByTitleOrderByCreateDateTimeDesc(title, page, size);
        }
        return draftService.findArticleDraftsOrderByCreateDateTimeDesc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByCreateDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByAuthorAndTitleOrderByCreateDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return draftService.findArticleDraftsByAuthorOrderByCreateDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByTitleOrderByCreateDateTimeAsc(title, page, size);
        }
        return draftService.findArticleDraftsOrderByCreateDateTimeAsc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeDesc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeDesc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return draftService.findArticleDraftsByAuthorOrderByLastModifiedDateTimeDesc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByTitleOrderByLastModifiedDateTimeDesc(title, page, size);
        }
        return draftService.findArticleDraftsOrderByLastModifiedDateTimeDesc(page, size);
    }

    private Page<ArticleDraft> searchArticleDraftsByAuthorOrTitleOrderByLastModifiedDateTimeAsc(String author, String title, int page, int size) {
        if (!UtilService.isNullOrEmpty(author) && !UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByAuthorAndTitleOrderByLastModifiedDateTimeAsc(author, title, page, size);
        } else if (!UtilService.isNullOrEmpty(author)) {
            return draftService.findArticleDraftsByAuthorOrderByLastModifiedDateTimeAsc(author, page, size);
        } else if (!UtilService.isNullOrEmpty(title)) {
            return draftService.findArticleDraftsByTitleOrderByLastModifiedDateTimeAsc(title, page, size);
        }
        return draftService.findArticleDraftsOrderByLastModifiedDateTimeAsc(page, size);
    }
}
