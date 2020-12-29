package net.stevencai.blog.backend.response;

import lombok.Data;
import net.stevencai.blog.backend.clientResource.ArticleResource;

@Data
public class ArticleResponse {
    private ArticleResource articleResource;
    private boolean status = false;

    public ArticleResponse() {
    }

    public ArticleResponse(ArticleResource articleResource) {
        this.articleResource = articleResource;
        this.status = true;
    }
}
