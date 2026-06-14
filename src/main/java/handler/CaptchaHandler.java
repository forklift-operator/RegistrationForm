package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.CaptchaDto;
import service.CaptchaService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CaptchaHandler implements HttpHandler {
    private final Gson gson = new Gson();
    public static final Map<String, String> captchaStore = new ConcurrentHashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            String[] captcha = CaptchaService.generateCaptcha();
            String question = captcha[0];
            String answer = captcha[1];
            String captchaId = UUID.randomUUID().toString();

            captchaStore.put(captchaId, answer);
            byte[] bytes = gson.toJson(new CaptchaDto(captchaId, question)).getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
