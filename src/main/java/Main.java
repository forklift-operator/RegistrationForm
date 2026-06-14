import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    static void main()  throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/register", new RegistrationHandler());

        server.setExecutor(null);

        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
