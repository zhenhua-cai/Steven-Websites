package net.stevencai.stevenweb.web.controller;

import net.stevencai.stevenweb.frontendResource.ArticleResource;
import net.stevencai.stevenweb.service.ArticleService;
import net.stevencai.stevenweb.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/articles")
public class ArticleController {
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

    @Autowired
    public void setPostService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/new")
    public String createNewArticle(Model model, Principal principal){
        ArticleResource articleResource = new ArticleResource();
        articleResource.setId(appUtil.generateUUIDForArticle(principal.getName()));
        model.addAttribute("article",articleResource);
        return "editArticle";
    }

    @GetMapping("/{id}")
    public String showArticle(@PathVariable String id, Model model){
        ArticleResource articleResource = null;
        if(!model.containsAttribute("article")){
            articleResource = articleService.findArticleById(id);
        }
        else{
            articleResource =(ArticleResource) model.getAttribute("article");
        }
        model.addAttribute("article", articleResource);
        return "article";
    }
    @PostMapping("/publish")
    @ResponseStatus(value= HttpStatus.CREATED)
    public void publishArticle(ArticleResource articleResource){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        articleResource.setUsername(authentication.getName());
        if(articleResource.getId() != null && !articleResource.getId().isEmpty()){
            articleService.deleteArticleDraftByIdIfExists(articleResource.getId());
        }
        articleService.saveArticle(articleResource);
    }
    @PostMapping("/saveArticle")
    @ResponseStatus(value= HttpStatus.OK)
    public void savePost(ArticleResource articleResource, Principal principal){
        articleResource.setUsername(principal.getName());
        articleService.saveDraft(articleResource);
    }
}
