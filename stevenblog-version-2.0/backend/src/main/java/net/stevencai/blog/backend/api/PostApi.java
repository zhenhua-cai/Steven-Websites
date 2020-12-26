package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticleResourceResponse;
import net.stevencai.blog.backend.clientResource.ResponsePage;
import net.stevencai.blog.backend.entity.Article;
import net.stevencai.blog.backend.entity.ArticleDraft;
import net.stevencai.blog.backend.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PostApi {
    protected ArticleResourceResponse constructArticleResourceResponse(Page<? extends Post> pageable) {
        List<ArticleResource> articles = pageable
                .get()
                .map(ArticleResource::new)
                .collect(Collectors.toList());
        ResponsePage responsePage = new ResponsePage();
        responsePage.setNumber(pageable.getNumber());
        responsePage.setSize(pageable.getSize());
        responsePage.setTotalElements(pageable.getTotalElements());
        responsePage.setTotalPages(pageable.getTotalPages());
        return new ArticleResourceResponse(articles, responsePage);
    }
}
