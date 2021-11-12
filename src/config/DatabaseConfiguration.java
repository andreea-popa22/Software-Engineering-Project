package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static final String DB_URL = "jdbc:mysql://isdatabase.cgkzodhucsvh.us-east-2.rds.amazonaws.com:3306/?user=admin";
    private static final String USER = "admin";
    private static final String PASSWORD = "ISadmin2021";

    private static Connection databaseConnection;

    private DatabaseConfiguration() {
    }

    public static Connection getDatabaseConnection() {
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                databaseConnection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                if(databaseConnection!=null)
                {
                    System.out.println("Connected!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return databaseConnection;
    }

    public static void closeDatabaseConnection() {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
                System.out.println("Disconnected!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
