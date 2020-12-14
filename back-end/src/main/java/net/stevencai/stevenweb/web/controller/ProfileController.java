package net.stevencai.stevenweb.web.controller;

import net.stevencai.stevenweb.entity.Article;
import net.stevencai.stevenweb.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private ArticleService articleService;

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String getProfile(Model Model){

        return "profile";
    }
    @GetMapping("articles")
    public @ResponseBody Page<Article> fetchUserArticles(@RequestParam("page")int page,
                                                         @RequestParam("size")int size,
                                                         @RequestParam("title") String title
                                                         ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(title == null|| title.isEmpty()) {
            return articleService.findArticlesByUsername(authentication.getName(), page, size);
        }
        return articleService.findArticlesByUsernameAndTitle(authentication.getName(), title,page, size);
    }
}
