package net.stevencai.stevenweb.util;

import com.fasterxml.uuid.Generators;
import net.stevencai.stevenweb.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppUtil {
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String createVerificationToken(User user){
        UUID uuid = Generators.timeBasedGenerator().generate();
        byte[] emailStrBytes =  passwordEncoder.encode(user.getEmail()).getBytes();
        return byteArrayToHexString(emailStrBytes)+uuid.toString()+generateTimeBasedRandomString();
    }

    public static String generateTimeBasedRandomString(){
        String raw = LocalDateTime.now().toString();
        byte[] rawStringBytes = raw.getBytes();
        return byteArrayToHexString(rawStringBytes);
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

    public static String maskEmail(String email){
        StringBuilder maskedEmail = new StringBuilder();
        maskedEmail.append(email.charAt(0));
        int atSymbolIndex = email.indexOf("@");
        for(int i =1; i< atSymbolIndex;i++){
            maskedEmail.append("*");
        }
        maskedEmail.append(email.substring(atSymbolIndex));
        return maskedEmail.toString();
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
}
