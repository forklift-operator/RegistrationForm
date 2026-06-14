import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.SessionService;

import java.io.IOException;

public class LogoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("POST")) {
            String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
            String sessionId = extractSessionId(cookieHeader);

            SessionService.invalidateSession(sessionId);

            exchange.getResponseHeaders().add("Set-Cookie", "session_id=; Path=/; HttpOnly; Max-Age=0");
            exchange.sendResponseHeaders(200, -1);
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
}
