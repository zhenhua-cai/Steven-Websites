package net.stevencai.blog.backend.clientResource;

import lombok.Data;

import java.util.List;

@Data
public class ArticlesListResponse {
    private List<ArticleResource> articles;
    private ResponsePage responsePage;

    public ArticlesListResponse() {
    }

    public ArticlesListResponse(List<ArticleResource> articles, ResponsePage responsePage) {
        this.articles = articles;
        this.responsePage = responsePage;
    }
}
