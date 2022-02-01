
package com.agira.Agira;
import com.agira.Agira.Entities.*;
//import org.json.simple.JSONObject;
import com.agira.Agira.Repositories.*;
import com.agira.Agira.Services.ApiService;
import com.agira.Agira.Services.Service;
import org.eclipse.paho.client.mqttv3.*;
//import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
@RestController
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


    @PostMapping("/process_register")
    public User processRegister(@RequestBody User user) {
        System.out.println(user.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> listUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/addPurifier")
    public String addPurifier(@RequestBody Purifier purifier) {
        purifier.setSchedule_id(1);
        purifierRepository.save(purifier);
        return "Added Purifier Successfully!";
    }

    @PostMapping("/process_edit_profile")
    public User processEditProfile(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
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

        return user1;
    }

    @GetMapping("/profile")
    public User viewProfile() {
        return getUser();
    }

    @PostMapping("/addAudio")
    public String addAudio(@RequestBody Audio audio) {
            audioRepository.save(audio);
            return "Added Audio Successfully!";
    }

    @PostMapping("/addMessage")
    public String addMessage(@RequestBody Message message) {
        messageRepository.save(message);
        return "Added Message Successfully!";
    }

    @GetMapping("/getPurifierState")
    public Purifier getPurifierState(){
        return getPurifier();
    }

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
        return "Trigger successful!";
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
        return purifierRepository.findById(user.getPurifier_id());
    }

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

    @PutMapping("/pickOffSchedule")
    public String pickOffSchedule(){
        pickSchedule(2);
        return "Your purifier is off!";
    }

    @PutMapping("/pickOnSchedule")
    public String pickOnSchedule(){
        pickSchedule(1);
        return "Your purifier is on!";
    }

    @PutMapping("/pickDaySchedule")
    public String pickDaySchedule(){
        pickSchedule(4);
        return "Your purifier is on day schedule (7:00 - 19:00)!";
    }

    @PutMapping("/pickNightSchedule")
    public String pickNightSchedule(){
        pickSchedule(3);
        return "Your purifier is on night schedule (19:00 - 7:00)!";
    }

    @PutMapping("/editPurifierLocation")
    public Purifier editPurifierLocation(@RequestParam String location){
        Purifier purifier2 = getPurifier();
        purifier2.setLocation_name(location);
        purifierRepository.save(purifier2);
        return purifier2;
    }

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
        return "Purifier lights game on!";
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
        checkSchedule(purifier);
        purifierRepository.save(purifier);
    }

    @ResponseBody
    @GetMapping("/statistics")
    public String publishTopic() throws MqttException {
        // get parameters values from Air Quality Api
        Purifier purifier = getPurifierNoSchedule();
        String city = purifier.getLocation_name();
        String co = "CO: " + ApiService.GetParameter(city, "co") + " ;\n";
        String no2 = "NO2: " + ApiService.GetParameter(city, "no2") + " ;\n";
        String o3 = "Ozone: " + ApiService.GetParameter(city, "o3") + " ;\n";
        String so2 = "SO2: " + ApiService.GetParameter(city, "so2") + " ;\n";
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
        statistics.setNo2(no2);
        statistics.setOzone(so2);
        statistics.setSo2(so2);
        statistics.setTimestamp(new Time(System.currentTimeMillis()));
        addStatistics(statistics);

        return msg;
    }

    public void addStatistics(Statistics statistic) {
        statistic.setPurifier_id(getPurifierNoSchedule().getPurifier_id());
        statisticsRepository.save(statistic);
    }

}



// http://localhost:8080/swagger-ui.html
            