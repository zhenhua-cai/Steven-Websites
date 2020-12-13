package net.stevencai.stevenweb.web.controller;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.frontendResource.ArticleResource;
import net.stevencai.stevenweb.service.ArticleService;
import net.stevencai.stevenweb.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createNewArticle(Model model){
        ArticleResource articleResource = new ArticleResource();

        model.addAttribute("article",articleResource);
        return "editArticle";
    }
    @PostMapping("/publish")
    public String publishArticle(ArticleResource articleResource,
                                 BindingResult bindingResult,
                                 RedirectAttributes attributes
                              ){
        if(bindingResult.hasErrors()){
            return "editArticle";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        articleResource.setUsername(authentication.getName());

        if(articleResource.getId() != null && !articleResource.getId().isEmpty()){
            articleService.deleteArticleDraftById(articleResource.getId());
        }
        else{
            articleResource.setId(appUtil.generateUUIDForArticle(authentication.getName()));
        }

        articleResource = articleService.saveArticle(articleResource);
        attributes.addFlashAttribute("article", articleResource);
        return "redirect:"+ articleResource.getId();
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


}
