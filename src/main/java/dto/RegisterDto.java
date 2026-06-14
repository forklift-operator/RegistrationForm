package dto;

public record RegisterDto(
        String name,
        String email,
        String password,
        String captcha
) {

    public RegisterDto {
        if (name.isBlank()) throw new IllegalArgumentException("Name cannot be blank or null");
        if (email.isBlank()) throw new IllegalArgumentException("Email cannot be blank or null");
        if (password.isBlank()) throw new IllegalArgumentException("Password cannot be blank or null");
        if (captcha.isBlank()) throw new IllegalArgumentException("Invalid captcha");
    }
}
