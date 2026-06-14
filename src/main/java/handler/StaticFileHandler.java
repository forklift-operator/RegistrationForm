package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticFileHandler implements HttpHandler {
    private final String filePath;

    public StaticFileHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try (InputStream is = getClass().getResourceAsStream("/public" + filePath)) {
            if (is == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] bytes = is.readAllBytes();

            if (filePath.endsWith(".html")) {
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            }

            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

    }
}
