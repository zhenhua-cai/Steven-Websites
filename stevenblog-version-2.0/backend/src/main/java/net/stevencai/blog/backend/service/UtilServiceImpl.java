package net.stevencai.blog.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource({
        "classpath:articles.properties",
        "classpath:application.properties"
})
public class UtilServiceImpl implements UtilService {
    private Environment env;

    @Value("${JWT_RAW_SECRET}")
    private String jwtRawSecret;

    private String jwtSecret;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Override
    public String getArticlesBasePath() {
        return env.getProperty("app.articles.base.path");
    }

    @Override
    public String getJwtSecret() {
        if(jwtSecret != null){
            return jwtSecret;
        }
        generateJwtSecret();
        return jwtSecret;
    }

    private void generateJwtSecret(){
        byte[] bytes= jwtRawSecret.getBytes();
        jwtSecret = byteArrayToHexString(bytes);
    }

    private static byte[] hexStringToByteArray(String s){
        byte[] bytes = new byte[s.length()/2];
        for(int i = 0;i< bytes.length;i++){
            int index = i*2;
            int v = Integer.parseInt(s.substring(index,index+2),16);
            bytes[i] =(byte)v;
        }
        return bytes;
    }

    public static String byteArrayToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for(byte aByte : bytes){
            int v = aByte & 0xff;
            if(v < 16){
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
}
