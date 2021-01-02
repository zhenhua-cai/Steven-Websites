package net.stevencai.blog.backend.service;

import com.fasterxml.uuid.Generators;
import net.stevencai.blog.backend.exception.InvalidArticlePathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@PropertySource({
        "classpath:articles.properties",
        "classpath:application.properties"
})
public class UtilServiceImpl implements UtilService {
    private Environment env;
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.raw.secret}")
    private String jwtRawSecret;

    private String jwtSecret;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getArticlesBasePath() {
        return env.getProperty("app.articles.base.path");
    }

    @Override
    public String getArticleDraftsBasePath() {
        return env.getProperty("app.drafts.base.path");
    }

    @Override
    public String convertDraftPathToArticlePath(String draftPath) {
        String draftBasePath = getArticleDraftsBasePath();
        if (!draftPath.startsWith(draftBasePath)) {
            throw new InvalidArticlePathException("Article draft path is invalid: " + draftPath);
        }
        return getArticlesBasePath() + File.separator + draftPath.substring(draftBasePath.length());
    }

    @Override
    public String convertArticlePathToDraftPath(String articlePath) {
        String articleBasePath = getArticlesBasePath();
        if (!articlePath.startsWith(articleBasePath)) {
            throw new InvalidArticlePathException("Article draft path is invalid: " + articlePath);
        }
        return getArticlesBasePath() + File.separator + articlePath.substring(articleBasePath.length());
    }

    @Override
    public String getJwtSecret() {
        if (jwtSecret != null) {
            return jwtSecret;
        }
        generateJwtSecret();
        return jwtSecret;
    }

    @Override
    public String generateUUIDForArticle(String username) {
        UUID uuid = Generators.timeBasedGenerator().generate();
        byte[] userBytes = passwordEncoder.encode(username).getBytes();
        return byteArrayToHexString(userBytes) + uuid.toString() + generateTimeBasedRandomString();
    }

    @Override
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null) {
            return request.getRemoteAddr();
        }
        return ip;
    }

    private void generateJwtSecret() {
        byte[] bytes = jwtRawSecret.getBytes();
        jwtSecret = byteArrayToHexString(bytes);
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            bytes[i] = (byte) v;
        }
        return bytes;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            int v = aByte & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    public static String generateTimeBasedRandomString() {
        String raw = LocalDateTime.now().toString();
        byte[] rawStringBytes = raw.getBytes();
        return byteArrayToHexString(rawStringBytes);
    }

}
