package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CaptchaServiceTest {

    @Test
    public void testGenerateCaptcha() {
        String[] captcha = CaptchaService.generateCaptcha();

        assertEquals(2, captcha.length);
        assertNotNull(captcha[0]);
        assertNotNull(captcha[1]);

        assertDoesNotThrow(() -> Integer.parseInt(captcha[1]));
    }
}