package dto;

public record UserLoginDto(
        String email,
        String password,
        String captchaId,
        String captchaAnswer
) {
}