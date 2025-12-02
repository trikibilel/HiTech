package tn.hitech.Database.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hitech";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    private DbConnection() {
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Database connection failed!");
        }
        return connection;
    }
}
