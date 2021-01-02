package net.stevencai.blog.backend.api;

import net.stevencai.blog.backend.clientResource.ArticleResource;
import net.stevencai.blog.backend.clientResource.ArticlesListResponse;
import net.stevencai.blog.backend.clientResource.ResponsePage;
import net.stevencai.blog.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostApi {
    protected ArticlesListResponse constructArticleResourceResponse(Page<? extends Post> pageable) {
        List<ArticleResource> articles = pageable
                .get()
                .map(ArticleResource::new)
                .collect(Collectors.toList());
        ResponsePage responsePage = new ResponsePage();
        responsePage.setNumber(pageable.getNumber());
        responsePage.setSize(pageable.getSize());
        responsePage.setTotalElements(pageable.getTotalElements());
        responsePage.setTotalPages(pageable.getTotalPages());
        return new ArticlesListResponse(articles, responsePage);
    }
}
