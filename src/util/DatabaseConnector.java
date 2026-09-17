package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL =
           private static final String PASSWORD = System.getenv("HOSPITALITY_DB_PASSWORD");
    private static final String USER = "hospitality_app";pwd

    private static final String PASSWORD = "Abhishek2002";

   public static Connection getConnection() throws SQLException {
    if (PASSWORD == null || PASSWORD.isBlank()) {
        throw new SQLException("HOSPITALITY_DB_PASSWORD environment variable is not set.");
    }

    return DriverManager.getConnection(URL, USER, PASSWORD);
}
}