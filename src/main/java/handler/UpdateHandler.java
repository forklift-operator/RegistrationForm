package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ExceptionDto;
import dto.UserResponseDto;
import dto.UserUpdateDto;
import model.User;
import repository.UserRepository;
import service.SessionService;
import service.ValidateService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class UpdateHandler implements HttpHandler {
    private static final Gson gson = new Gson();
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("PUT")) {
            try (InputStream is = exchange.getRequestBody()) {
                String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
                String sessionId = extractSessionId(cookieHeader);
                String email = SessionService.getEmailBySession(sessionId);

                if (email == null) {
                    sendResponse(exchange, 401, new ExceptionDto("Unauthorized: Please login first"));
                    return;
                }

                UserUpdateDto userDto = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), UserUpdateDto.class);

                User user = userRepository.findByEmail(email);
                if (user == null) {
                    sendResponse(exchange, 444, new ExceptionDto("User not found"));
                    return;
                }

                if (userDto.password() != null) {
                    ValidateService.validatePassword(userDto.password());
                }

                String newName = userDto.name() == null ? user.getName() : userDto.name();
                //hash the pass
                String newPassword = userDto.password() == null ? user.getPassword() : userDto.password();

                if (userRepository.updateUser(newName, email, newPassword)) {
                    sendResponse(exchange, 200, new UserResponseDto(user.getId(), newName, email));
                } else {
                    sendResponse(exchange, 500, new ExceptionDto("Failed to update profile data"));
                }

            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, new ExceptionDto(e.getMessage()));
            } catch (SQLException e) {
                sendResponse(exchange, 500, new ExceptionDto("Database error: " + e.getMessage()));
            }
        } else {
            sendResponse(exchange, 405, new ExceptionDto("Method Not Allowed"));
        }
    }

    private void sendResponse(HttpExchange exchange, int status, Object object) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = gson.toJson(object).getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
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
}
