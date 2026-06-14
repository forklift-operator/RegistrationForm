package service;

import dto.RegisterUserDto;

import java.util.regex.Pattern;

public class ValidateService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void validateCredentials(RegisterUserDto userDto) {
        String name = userDto.name();
        String email = userDto.email();
        String password = userDto.password();
        String captcha = userDto.captcha();

        if (name.isBlank()) throw new IllegalArgumentException("Name cannot be blank or null");
        if (email.isBlank()) throw new IllegalArgumentException("Email cannot be blank or null");
        if (password.isBlank()) throw new IllegalArgumentException("Password cannot be blank or null");
        if (captcha.isBlank()) throw new IllegalArgumentException("Invalid captcha");

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
}
