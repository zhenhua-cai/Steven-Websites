package net.stevencai.blog.backend.clientResource;

import lombok.Data;

import java.util.List;

@Data
public class ArticleResourceResponse {
    private List<ArticleResource> articles;
    private ResponsePage responsePage;

    public ArticleResourceResponse() {
    }

    public ArticleResourceResponse(List<ArticleResource> articles, ResponsePage responsePage) {
        this.articles = articles;
        this.responsePage = responsePage;
    }
}
