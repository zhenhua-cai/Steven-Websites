package net.stevencai.stevenweb.web.controller;

import net.stevencai.stevenweb.frontendResource.ArticleResource;
import net.stevencai.stevenweb.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"/","/home"})
public class HomeController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String home(Model model){
        List<ArticleResource> articleResources = articleService.findRecentArticlesTitles();
        model.addAttribute("articles", articleResources);
        return "index";
    }
    @GetMapping("about")
    public String aboutMe(){
        return "about";
    }
    @GetMapping("contact")
    public String contactMe(){
        return "contact";
    }

}
