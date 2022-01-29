
package com.agira.Agira;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.simple.JSONObject;
import org.springframework.boot.configurationprocessor.json.*;
//import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@SpringBootApplication
@Controller
@EnableSwagger2
public class DemoApplication {
    public static void main(String[] args) throws SQLException {

        SpringApplication.run(DemoApplication.class, args);
        try {
            URL url = new URL(SensitiveInformation.apiURL);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }
            else {
                String informationString = ApiService.ReadData(url);
                JSONObject dataObject = new JSONObject(informationString);
                ObjectMapper mapper = new ObjectMapper();
                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataObject));
                System.out.println(ApiService.GetCO("bucharest"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PurifierRepository purifierRepository;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        try {
            String jdbcURL = SensitiveInformation.databaseJDBC;
            String username = SensitiveInformation.databaseUser;
            String password = SensitiveInformation.databasePassword;

            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            //String sql = "INSERT INTO MESSAGE (message_text) VALUES ('ABC')";

            //Statement statement = connection.createStatement();

            //int rows = statement.executeUpdate(sql);
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
    public String processRegister(@RequestBody User user) {
        System.out.println(user.getUsername());
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

    @PostMapping("/addPurifier")
    public String addPurifier(@RequestBody Purifier purifier) {

        purifierRepository.save(purifier);

        return "register_succ";
    }


    @PostMapping("/process_edit_profile")
    public String processEditProfile(@RequestBody User user) {
        // System.out.println(user.getUsername());
        User user1 = userRepo.findByUsername(user.getUsername());
        if(user.getPassword() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user1.setPassword(encodedPassword);
        }

        user1.setUsername(user.getUsername());
        user1.setDate_of_birth(user.getDate_of_birth());
        user1.setAfflictions(user.getAfflictions());
        userRepo.save(user1);

        return "register_succ";
    }

    @GetMapping("/profile")
    public String viewProfile() {

        return "profile";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model) {
        model.addAttribute("user", new User());
        return "editProfile";
    }

}

// http://localhost:8080/hello
            