package net.stevencai.blog.backend.service;


public interface UtilService {
    String getArticlesBasePath();
    String getArticleDraftsBasePath();
    String convertDraftPathToArticlePath(String draftPath);
    String convertArticlePathToDraftPath(String articlePath);

    String getJwtSecret();

    String generateUUIDForArticle(String username);

    static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
