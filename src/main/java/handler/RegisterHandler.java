package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ExceptionDto;
import dto.UserRegisterDto;
import dto.UserResponseDto;
import repository.UserRepository;
import service.ValidateService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {

    private final Gson gson = new Gson();
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if (requestMethod.equals("POST")) {
            try (InputStream is = exchange.getRequestBody()) {
                UserRegisterDto userDto = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), UserRegisterDto.class);

                ValidateService.validateRegisterDto(userDto);
                ValidateService.validateCaptcha(userDto.captchaId(), userDto.captchaAnswer());

                if (userRepository.existsByEmail(userDto.email())) {
                    sendResponse(exchange, 400, new ExceptionDto("Email already exists"));
                }

                Long userId = userRepository.saveUser(userDto);

                UserResponseDto user = new UserResponseDto(userId, userDto.name(), userDto.email());

                sendResponse(exchange, 200, user);
            } catch (IllegalArgumentException e) {
                ExceptionDto exceptionDto = new ExceptionDto(e.getMessage());
                sendResponse(exchange, 400, exceptionDto);

            } catch (SQLException e) {
                ExceptionDto exceptionDto = new ExceptionDto("Database error: " + e.getMessage());
                sendResponse(exchange, 500, exceptionDto);

            } catch (Exception e) {
                sendResponse(exchange, 500, new ExceptionDto("Server error: " + e.getMessage()));
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
