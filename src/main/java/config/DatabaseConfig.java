package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/telebid_db";
    private static final String USER = "root";
    private static final String PASSWORD = "ba38x5e66";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static Connection initializeSchema() {
        String schema = "CREATE TABLE IF NOT EXISTS users (" +
                "    id INT AUTO_INCREMENT PRIMARY KEY," +
                "    name VARCHAR(50) NOT NULL," +
                "    password VARCHAR(50) NOT NULL," +
                "    email VARCHAR(100)" +
                ");";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(schema);
            return conn;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
