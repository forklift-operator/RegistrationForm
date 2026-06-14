package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ExceptionDto;
import dto.UserLoginDto;
import dto.UserResponseDto;
import model.User;
import repository.UserRepository;
import service.SessionService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class LoginHandler implements HttpHandler {
    Gson gson = new Gson();
    UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("POST")) {
            try (InputStream is = exchange.getRequestBody()) {
                UserLoginDto userDto = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), UserLoginDto.class);


                User user = userRepository.findByEmail(userDto.email());

                if (user == null || !user.getPassword().equals(userDto.password())) {
                    throw new IllegalArgumentException("Wrong credentials");
                }

                String sessionId = SessionService.createSession(user.getEmail());
                String cookieValue = String.format("session_id=%s; Path=/; HttpOnly; Max-Age=3600", sessionId);
                exchange.getResponseHeaders().add("Set-Cookie", cookieValue);

                sendResponse(exchange, 200, new UserResponseDto(user.getId(), user.getName(), user.getEmail()));

            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, new ExceptionDto(e.getMessage()));
            } catch (SQLException e) {
                sendResponse(exchange, 500, new ExceptionDto("Database error: " + e.getMessage()));
            }
        } else {
            ExceptionDto exceptionDto = new ExceptionDto("Invalid request method");
            sendResponse(exchange, 405, exceptionDto);
        }
    }

    private void sendResponse(HttpExchange exchange, int status, Object object) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        String jsonString = gson.toJson(object);
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
