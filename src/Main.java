import config.DatabaseConfiguration;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("hi");
        Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
    }
}
