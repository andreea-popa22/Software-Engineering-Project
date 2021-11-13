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

public class Register {
    public static void selectTest(){
        try {
            Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
            Statement stmt = databaseConnection.createStatement();
            ResultSet rs2 = stmt.executeQuery("USE ISdatabase");
            ResultSet rs = stmt.executeQuery("SELECT * FROM LOCATION");
            while (rs.next()){
                System.out.println("ID: " + rs.getInt("location_id") + ", " + "Nume locatie: " + rs.getString("location_name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void registerUser(){
        try {
            Connection databaseConnection = DatabaseConfiguration.getDatabaseConnection();
            Statement stmt2 = databaseConnection.createStatement();
            ResultSet rs = stmt2.executeQuery("USE ISdatabase");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Username: ");
            String userName = scanner.nextLine();

            System.out.println("Password: ");
            String password = scanner.nextLine();
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String s = Base64.getEncoder().encodeToString(hashedPassword);
            System.out.println(s);

            System.out.println("Date Of Birth: ");
            String dateOfBirthString = scanner.nextLine();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateOfBirth = formatter.parse(dateOfBirthString);
            java.sql.Date sqlDateOfBirth = new java.sql.Date(dateOfBirth.getTime());
            System.out.println("Gender(f or m): ");
            String gender = scanner.nextLine();
            System.out.println("Ailments: ");
            String ailments = scanner.nextLine();
            String query = "INSERT INTO USER (username, purifier_id, password, date_of_birth, gender, ailments, salt) " + "values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = databaseConnection.prepareStatement(query);
            stmt.setString(1, userName);
            stmt.setInt(2, 2);
            stmt.setString(3, s);
            stmt.setDate(4, sqlDateOfBirth);
            stmt.setString(5, gender);
            stmt.setString(6, ailments);
            stmt.setBytes(7, salt);
            stmt.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
