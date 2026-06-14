package service;

import dto.UserLoginDto;
import dto.UserRegisterDto;
import handler.CaptchaHandler;

import java.util.regex.Pattern;

public class ValidateService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void validateRegisterDto(UserRegisterDto userDto) {
        String name = userDto.name();
        String email = userDto.email();
        String password = userDto.password();

        if (name.isBlank()) throw new IllegalArgumentException("Name cannot be blank or null");
        if (email.isBlank()) throw new IllegalArgumentException("Email cannot be blank or null");
        if (password.isBlank()) throw new IllegalArgumentException("Password cannot be blank or null");

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 symbols");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must include at least 1 numerical symbol");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must include at least 1 capital letter");
        }
    }

  public static void validateCaptcha(UserRegisterDto userDto) {
        String answer = CaptchaHandler.captchaStore.get(userDto.captchaId());
        if (answer == null) {
            throw new IllegalArgumentException("Invalid captcha id");
        }

        CaptchaHandler.captchaStore.remove(userDto.captchaId());

        if (!answer.equals(userDto.captchaAnswer().trim())) {
            throw new IllegalArgumentException("Invalid captcha");
        }
    }
}
