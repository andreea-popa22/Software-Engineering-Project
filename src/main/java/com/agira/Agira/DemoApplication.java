
package com.agira.Agira;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.simple.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Qualifier;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.boot.configurationprocessor.json.*;
//import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
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
import java.util.Calendar;
import java.util.List;

@SpringBootApplication
@Controller
@EnableSwagger2
//@ApiOperation(value = "Update registration detail",
//        authorizations = { @Authorization(value="basicAuth") })
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

    @Autowired
    private StatisticsRepository statisticsRepository;

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
    public String processEditProfile(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        // System.out.println(user.getUsername());
        User user1 = userRepo.findByUsername(currentPrincipalName);
        if(user.getPassword() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user1.setPassword(encodedPassword);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        System.out.println(currentPrincipalName + "!!!!!");
        System.out.println(userRepo.findByUsername(currentPrincipalName));
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

    @ResponseBody
    @PutMapping("/turnOnLightsGame")
    public String turnOnLightsGame(){
        String color = Service.colorGame();
        Purifier purifier = getPurifierNoSchedule();
        for (int i = 0 ; i < 10; i ++)
        {
            purifier.setHex(color);
            purifierRepository.save(purifier);
            System.out.println("Purifier lights game on!" + color);
            color = Service.colorGame();
        }
        return "Purifier lights game on!" + color;
    }

    @ResponseBody
    @GetMapping("/statistics")
    public String publishTopic() throws MqttException {
        // get parameters values from Air Quality Api
        Purifier purifier = getPurifierNoSchedule();
        String city = purifier.getLocation_name();
        String co = "CO: " + ApiService.GetParameter(city, "co") + " ;\n";
        String no2 = "Dust: " + ApiService.GetParameter(city, "no2") + " ;\n";
        String o3 = "Ozone: " + ApiService.GetParameter(city, "o3") + " ;\n";
        String so2 = "Humidity: " + ApiService.GetParameter(city, "so2") + " ;\n";
        String msg = co + no2 + o3 + so2;

        // Mqtt connection
        String broker = "tcp://localhost:1883";
        String topicName = "mytopic";
        int qos = 1;

        MqttClient mqttClient = new MqttClient(broker,String.valueOf(System.nanoTime()));
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setKeepAliveInterval(1000);

        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        message.setRetained(true);
        MqttTopic topic2 = mqttClient.getTopic(topicName);
        mqttClient.connect(connOpts);
        topic2.publish(message);

        // Create statistics object to save in database
        Statistics statistics = new Statistics();
        statistics.setCo(co);
        statistics.setDust(no2);
        statistics.setOzone(so2);
        statistics.setHumidity(so2);
        statistics.setTimestamp(new Time(System.currentTimeMillis()));
        addStatistics(statistics);

        return msg;
    }

    public void addStatistics(Statistics statistic) {
        statistic.setPurifier_id(getPurifierNoSchedule().getPurifier_id());
        statisticsRepository.save(statistic);
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
        if(startTime.after(endTime)){
            is_on = time.after(schedule.getStart_time()) || time.before(schedule.getEnd_time());
        }
        else {
            is_on = time.after(schedule.getStart_time()) && time.before(schedule.getEnd_time());
        }
        purifier.setIs_on(is_on);
        purifierRepository.save(purifier);
        return is_on;
    }

    public void pickSchedule(int id){
        System.out.println("goood");
        Purifier purifier = getPurifierNoSchedule();
        purifier.setSchedule_id(id);
        purifierRepository.save(purifier);
    }

}



// http://localhost:8080/hello
            