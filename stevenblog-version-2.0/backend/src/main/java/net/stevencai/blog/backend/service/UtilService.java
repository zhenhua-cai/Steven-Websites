package net.stevencai.blog.backend.service;


public interface UtilService {
    public String getArticlesBasePath();

    String getJwtSecret();

    static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
