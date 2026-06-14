package dto;

public record UserRegisterDto(
        String name,
        String email,
        String password,
        String captcha
) {}
