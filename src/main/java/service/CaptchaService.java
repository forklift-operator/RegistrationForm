package service;

import java.util.Random;

public class CaptchaService {
    private static final Random random = new Random();

    public static String[] generateCaptcha() {
        int num1 = random.nextInt(20) + 1;
        int num2 = random.nextInt(20) + 1;

        String question = "Whats " + num1 + " + " + num2 + "?";
        String answer = String.valueOf(num1 + num2);
        return new String[]{question, answer};
    }
}
