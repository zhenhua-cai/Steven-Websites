package net.stevencai.stevenweb.web.api;

import net.stevencai.stevenweb.frontendResource.ArticleResource;
import net.stevencai.stevenweb.service.ArticleService;
import net.stevencai.stevenweb.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {

    private ArticleService articleService;
    private AppUtil appUtil;

    @Autowired
    public void setAppUtil(AppUtil appUtil) {
        this.appUtil = appUtil;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/saveArticle")
    public ResponseMessage savePost(@RequestBody ArticleResource articleResource){
        if(articleResource.getId() == null || articleResource.getId().isEmpty()){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            articleResource.setUsername(authentication.getName());
            articleResource.setId(appUtil.generateUUIDForArticle(authentication.getName()));
        }
        articleResource = articleService.saveDraft(articleResource);
        return new ResponseMessage(articleResource.getId());
    }


    private static class ResponseMessage{
        String id;

        public ResponseMessage() {
        }
        public ResponseMessage(String id) {
            this.id = id;

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
