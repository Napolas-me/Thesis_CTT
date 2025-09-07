package isel.meic.thesis.proto.db.dao;
import java.sql.*;
//Maybe use a configuration file or environment variables for better security
public class DBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/thesis";
    private static final String USER = "napolas";
    private static final String PASSWORD = "napolas";

    public static Connection connect()  {
        try {
            // Attempt to establish a connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // This block will be executed if a SQLException occurs during connection.
            // Common reasons include:
            // 1. Database server is not running (e.g., "Connection refused").
            // 2. Incorrect URL, username, or password.
            // 3. Database driver is not loaded (less common with modern JDBC).
            // 4. Network issues preventing connection to the database host.

            System.err.println("Failed to connect to the database!");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());

            // Wrap the SQLException in a RuntimeException to propagate it
            // or handle it gracefully (e.g., show a user-friendly message).
            throw new RuntimeException("Error connecting to the database. Possible reasons: DB offline, incorrect credentials, or network issues.", e);
        }
    }
}