import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.ExceptionDto;
import dto.RegisterDto;
import dto.UserResponseDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RegistrationHandler implements HttpHandler {

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if (requestMethod.equals("POST")) {
            try (InputStream is = exchange.getRequestBody()) {
                RegisterDto data = gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), RegisterDto.class);

                String name = data.name();
                String email = data.email();
                String password = data.password();
                String captcha = data.captcha();

                System.out.println(name + " " + email + " " + password + " " + captcha);



                UserResponseDto user = new UserResponseDto(1L, name, email);

                sendResponse(exchange, 200, user);
            } catch (IllegalArgumentException e) {
                ExceptionDto exceptionDto = new ExceptionDto(e.getMessage());
                sendResponse(exchange, 400, exceptionDto);

            } catch (Exception e) {
                ExceptionDto exceptionDto = new ExceptionDto("Malformed request or invalid data: " + e.getMessage());
                sendResponse(exchange, 400, exceptionDto);
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
