package Pages;

import config.DatabaseConfiguration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

public class Login {
    public static void loginUser(){
        try {
            Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
            Statement stmt2 = databaseConnection.createStatement();
            ResultSet rs = stmt2.executeQuery("USE ISdatabase");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Username: ");
            String userName = scanner.nextLine();

            System.out.println("Password: ");
            String password = scanner.nextLine();

            try{
                String selectUsername = "SELECT salt, password FROM USER WHERE username=?";
                PreparedStatement preparedStatement = databaseConnection.prepareStatement(selectUsername);
                preparedStatement.setString(1, userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    byte[] salt = resultSet.getBytes(1);
                    String dbPassword = resultSet.getString(2);

                    MessageDigest md = MessageDigest.getInstance("SHA-512");
                    md.update(salt);
                    byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                    String s = Base64.getEncoder().encodeToString(hashedPassword);

                    if (dbPassword.equals(s)){
                        System.out.println("You are logged in!");
                    }
                    else{
                        System.out.println("Incorrect password!");
                    }
                }
                else
                {
                    System.out.println("Username does not exist!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
