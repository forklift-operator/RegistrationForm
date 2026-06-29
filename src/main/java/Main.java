import com.sun.net.httpserver.HttpServer;
import handler.CaptchaHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.MeHandler;
import handler.RegisterHandler;
import handler.StaticFileHandler;
import handler.UpdateHandler;
import repository.UserRepository;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    static void main() throws IOException {
        UserRepository userRepository = new UserRepository();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/register", new RegisterHandler(userRepository));
        server.createContext("/login", new LoginHandler(userRepository));
        server.createContext("/logout", new LogoutHandler());
        server.createContext("/update", new UpdateHandler(userRepository));
        server.createContext("/captcha", new CaptchaHandler());
        server.createContext("/me", new MeHandler(userRepository));

        server.createContext("/", new StaticFileHandler("/index.html"));
        server.createContext("/login.html", new StaticFileHandler("/login.html"));
        server.createContext("/register.html", new StaticFileHandler("/register.html"));
        server.createContext("/profile", new StaticFileHandler("/profile.html"));
        server.setExecutor(null);

        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
