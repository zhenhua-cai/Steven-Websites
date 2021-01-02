package net.stevencai.blog.backend.clientResource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticlesListResponse implements Serializable {
    private List<ArticleResource> articles;
    private ResponsePage responsePage;

    public ArticlesListResponse() {
    }

    public ArticlesListResponse(List<ArticleResource> articles, ResponsePage responsePage) {
        this.articles = articles;
        this.responsePage = responsePage;
    }
}
