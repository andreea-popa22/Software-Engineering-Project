
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
@Controller
@EnableSwagger2

//@SecurityScheme(name = "agira", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
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
                ApiService.PrettyPrintJson(informationString);
                ApiService.GetParameter("london", "co");
                ApiService.GetParameter("london", "no2");
                ApiService.GetParameter("london", "o3");
                ApiService.GetParameter("london", "so2");
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

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

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
        purifier.setSchedule_id(1);

        purifierRepository.save(purifier);

        return "register_succ";
    }


    @PostMapping("/process_edit_profile")
    public String processEditProfile(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        // System.out.println(user.getUsername());
        User user1 = userRepo.findByUsername(currentPrincipalName);
        if(user.getPassword() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user1.setPassword(encodedPassword);
        }

        user1.setUsername(user.getUsername());
        user1.setDate_of_birth(user.getDate_of_birth());
        user1.setAfflictions(user.getAfflictions());
        userRepo.save(user1);

        return "profile";
    }

    @GetMapping("/profile")
    public String viewProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);
        return "profile";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model) {
        model.addAttribute("user", new User());
        return "editProfile";
    }

    @PostMapping("/addAudio")
    public String addAudio(@RequestBody Audio audio) {

            audioRepository.save(audio);

            return "register_succ";
    }

    @PostMapping("/addMessage")
    public String addMessage(@RequestBody Message message) {

        messageRepository.save(message);

        return "register_succ";
    }

    @ResponseBody
    @GetMapping("/getPurifierState")
    public Purifier getPurifierState(){
        return getPurifier();
    }

    @ResponseBody
    @PutMapping("/triggerAlarmAndMessage")
    public String triggerAlarmAndMessage(){
        User user = getUser();
        int limit = 30;
        Purifier purifier = getPurifier();
        if(user.getAfflictions().contains("astm")){
            limit = 40;
        }
        if(user.getAfflictions().contains("difterie")){
            limit = 50;
        }
        if(user.getAfflictions().contains("alergie")){
            limit = 60;
        }
        if(user.getAfflictions().contains("bronsita")){
            limit = 40;
        }
        purifier.setAudio_id(2);
        purifier.setMessage_id(2);
        purifierRepository.save(purifier);
        return "Trigger succesful!";
    }

    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepo.findByUsername(currentPrincipalName);
    }

    public Purifier getPurifier(){
        User user = getUser();
        Boolean is_on;
        Purifier purifier = purifierRepository.findById(user.getPurifier_id());
        is_on = checkSchedule(purifier);
        System.out.println(is_on);
        if(is_on){
            return purifier;
        }
        else {
            return null;
        }
    }

    public Purifier getPurifierNoSchedule(){
        User user = getUser();
        Boolean is_on;
        return purifierRepository.findById(user.getPurifier_id());

    }
    @ResponseBody
    @DeleteMapping("/deletePurifiers")
    public String deletePurifiers(){
        purifierRepository.deleteAll();
        return "delete all successful!";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@RequestBody Schedule schedule){
        scheduleRepository.save(schedule);

        return "register_succ";
    }
    @ResponseBody
    @PutMapping("/pickOffSchedule")
    public String pickOffSchedule(){
        pickSchedule(2);
        return "Your purifier is off!";
    }

    @ResponseBody
    @PutMapping("/pickOnSchedule")
    public String pickOnSchedule(){
        pickSchedule(1);
        return "Your purifier is on!";
    }

    @ResponseBody
    @PutMapping("/pickDaySchedule")
    public String pickDaySchedule(){
        pickSchedule(4);
        return "Your purifier is on day schedule (7:00 - 19:00)!";
    }

    @ResponseBody
    @PutMapping("/pickNightSchedule")
    public String pickNightSchedule(){
        pickSchedule(3);
        return "Your purifier is on night schedule (19:00 - 7:00)!";
    }

    @ResponseBody
    @PutMapping("/editPurifier")
    public String editPurifier(@RequestBody Purifier purifier){
        Purifier purifier2 = getPurifier();
        if(purifier.getLocation_name() != null){
            purifier2.setLocation_name(purifier.getLocation_name());
        }
        purifierRepository.save(purifier2);
        return "edit Purifier success!";
    }

    public Boolean checkSchedule(Purifier purifier){
        boolean is_on;
        Schedule schedule = scheduleRepository.findById(purifier.getSchedule_id());
        if(schedule == null){
            return false;
        }
        Time time = Time.valueOf(LocalTime.now());
        Time startTime = schedule.getStart_time();
        Time endTime = schedule.getEnd_time();
        System.out.println(time);
        System.out.println(schedule.getStart_time());
        System.out.println(schedule.getEnd_time());
        System.out.println(time.compareTo(schedule.getStart_time()) > 0);
        System.out.println(time.compareTo(schedule.getEnd_time()) < 0);
        if(startTime.after(endTime)){
            is_on = time.after(schedule.getStart_time()) || time.before(schedule.getEnd_time());
        }
        else {
            is_on = time.after(schedule.getStart_time()) && time.before(schedule.getEnd_time());
        }
        System.out.println("Aici");
        System.out.println(is_on);
        purifier.setIs_on(is_on);
        purifierRepository.save(purifier);
        return is_on;
    }

    public void pickSchedule(int id){
        Purifier purifier = getPurifierNoSchedule();
        purifier.setSchedule_id(id);
        purifierRepository.save(purifier);
    }

}



// http://localhost:8080/hello
            