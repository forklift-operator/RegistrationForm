package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ExceptionDto;
import model.User;
import repository.UserRepository;
import service.SessionService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MeHandler implements HttpHandler {
    private final Gson gson = new Gson();
    private final UserRepository userRepository;

    public MeHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if (requestMethod.equals("GET")) {
            try {
                String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
                String sessionId = extractSessionId(cookieHeader);
                String email = SessionService.getEmailBySession(sessionId);

                if (email == null) {
                    sendResponse(exchange, 401, new ExceptionDto("Unauthorised"));
                    return;
                }

                User user = userRepository.findByEmail(email);
                sendResponse(exchange, 200, user);
            } catch (Exception e) {
                sendResponse(exchange, 500, new ExceptionDto("Server error"));
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private String extractSessionId(String cookieHeader) {
        if (cookieHeader == null) return null;
        for (String cookie : cookieHeader.split(";")) {
            String[] pair = cookie.trim().split("=");
            if (pair.length == 2 && "session_id".equals(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    private void sendResponse(HttpExchange exchange, int status, Object object) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = gson.toJson(object).getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
