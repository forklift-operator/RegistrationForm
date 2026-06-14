package service;

import dto.UserLoginDto;
import dto.UserRegisterDto;
import handler.CaptchaHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateServiceTest {

    @BeforeEach
    public void setUp() {
        CaptchaHandler.captchaStore.clear();
    }

    @Test
    public void testRegister_Success() {
        UserRegisterDto dto = new UserRegisterDto("Stoyan Yordanov", "stoyan@gmail.com", "SecurePass1", "id", "5");
        assertDoesNotThrow(() -> ValidateService.validateRegisterDto(dto));
    }

    @Test
    public void testRegister_BlankName_ThrowsException() {
        UserRegisterDto dto = new UserRegisterDto("   ", "stoyan@gmail.com", "SecurePass1", "id", "5");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateRegisterDto(dto)
        );
        assertEquals("Name cannot be blank or null", exception.getMessage());
    }

    @Test
    public void testRegister_BlankEmail_ThrowsException() {
        UserRegisterDto dto = new UserRegisterDto("Stoyan", "", "SecurePass1", "id", "5");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateRegisterDto(dto)
        );
        assertEquals("Email cannot be blank or null", exception.getMessage());
    }

    @Test
    public void testRegister_InvalidEmailFormat_ThrowsException() {
        UserRegisterDto dto = new UserRegisterDto("Stoyan", "invalidEmail@", "SecurePass1", "id", "5");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateRegisterDto(dto)
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    public void testPassword_Blank_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validatePassword("")
        );
        assertEquals("Password cannot be blank or null", exception.getMessage());
    }

    @Test
    public void testPassword_TooShort_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validatePassword("Abc1")
        );
        assertEquals("Password must be at least 8 symbols", exception.getMessage());
    }

    @Test
    public void testPassword_MissingNumber_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validatePassword("NoNumber")
        );
        assertEquals("Password must include at least 1 numerical symbol", exception.getMessage());
    }

    @Test
    public void testPassword_MissingCapitalLetter_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validatePassword("alllowercase1")
        );
        assertEquals("Password must include at least 1 capital letter", exception.getMessage());
    }

    @Test
    public void testLogin_Success() {
        UserLoginDto dto = new UserLoginDto("stoyan@gmail.com", "Password123");
        assertDoesNotThrow(() -> ValidateService.validateLoginDto(dto));
    }

    @Test
    public void testLogin_BlankEmail_ThrowsException() {
        UserLoginDto dto = new UserLoginDto("", "Password123");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateLoginDto(dto)
        );
        assertEquals("Email is blank or null", exception.getMessage());
    }

    @Test
    public void testLogin_BlankPassword_ThrowsException() {
        UserLoginDto dto = new UserLoginDto("stoyan@gmail.com", "   ");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateLoginDto(dto)
        );
        assertEquals("Password is blank or null", exception.getMessage());
    }

    @Test
    public void testCaptcha_Success() {
        CaptchaHandler.captchaStore.put("uuid-123", "15");

        assertDoesNotThrow(() -> ValidateService.validateCaptcha("uuid-123", "15"));

        assertFalse(CaptchaHandler.captchaStore.containsKey("uuid-123"));
    }

    @Test
    public void testCaptcha_InvalidId_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateCaptcha("non-existent-id", "15")
        );
        assertEquals("Invalid captcha id", exception.getMessage());
    }

    @Test
    public void testCaptcha_WrongAnswer_ThrowsException() {
        CaptchaHandler.captchaStore.put("uuid-123", "15");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ValidateService.validateCaptcha("uuid-123", "99") // Грешен отговор
        );
        assertEquals("Invalid captcha", exception.getMessage());

        assertFalse(CaptchaHandler.captchaStore.containsKey("uuid-123"));
    }
}