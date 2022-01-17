
package com.agira.Agira;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@SpringBootApplication
@RestController
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        try {
            String jdbcURL = "jdbc:mysql://35.224.49.192:3306/IPdatabase?useSSL=false";
            String username = "root";
            String password = "123456";

            System.out.println("yess");

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            String sql = "INSERT INTO MESSAGE (message_text) VALUES ('ABC')";

            Statement statement = connection.createStatement();

            int rows = statement.executeUpdate(sql);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return String.format("Hello %s!", name);
    }
}
            