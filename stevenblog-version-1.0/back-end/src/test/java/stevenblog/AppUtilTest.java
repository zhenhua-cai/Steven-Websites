package stevenblog;

import net.stevencai.stevenweb.util.AppUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppUtilTest {

    @Test
    public void testMaskEmail(){
        String email = "abc123.abc@gmail.com";
        assertEquals("a*********@gmail.com", AppUtil.maskEmail(email));
    }
}
