import com.sun.net.httpserver.HttpServer;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.RegisterHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    static void main()  throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/register", new RegisterHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/logout", new LogoutHandler());
        server.createContext("/captcha", new LogoutHandler());

        server.setExecutor(null);

        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
