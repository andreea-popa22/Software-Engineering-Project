
package com.agira.Agira;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@SpringBootApplication
@Controller
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private UserRepository userRepo;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
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


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }


    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);


        userRepo.save(user);

        return "register_succ";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }



}

// http://localhost:8080/hello
            