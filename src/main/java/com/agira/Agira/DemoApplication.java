
package com.agira.Agira;
import com.agira.Agira.Entities.*;
import com.agira.Agira.Repositories.*;
import com.agira.Agira.Services.ApiService;
import com.agira.Agira.Services.LightsGameService;
import org.eclipse.paho.client.mqttv3.*;
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
                throw new RuntimeException("Api Connection HttpResponseCode: " + responseCode);
            }
//            else {
//                String informationString = ApiService.ReadData(url);
//                ApiService.PrettyPrintJson(informationString);
//            }
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
        if(purifierRepository.findById(user.getPurifier_id()) == null){
            return null;
        }
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
        User user1 = getUser();

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

    public int valuesLimits()
    {
        User user = getUser();
        int limit = 30;
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
        return limit;
    }

    //@PutMapping("/triggerAlarmAndMessage")
    public void triggerAlarmAndMessage(float co, float no2, float o3, float so2){
        int limit = valuesLimits();
        Purifier purifier = getPurifierNoSchedule();
        if (co < limit)
        {
            purifier.setMessage_id(1);
        }
        else
        {
            purifier.setAudio_id(1);
            purifier.setMessage_id(5);
        }
        if (no2 < limit)
        {
            purifier.setMessage_id(2);
        }
        else
        {
            purifier.setAudio_id(1);
            purifier.setMessage_id(6);
        }
        if (o3 < limit)
        {
            purifier.setMessage_id(3);
        }
        else
        {
            purifier.setAudio_id(1);
            purifier.setMessage_id(7);
        }
        if (so2 < limit)
        {
            purifier.setMessage_id(4);
        }
        else
        {
            purifier.setAudio_id(1);
            purifier.setMessage_id(8);
        }

        purifierRepository.save(purifier);
        //return "Trigger successful!";
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
        if(!getPurifierNoSchedule().is_on()){
            return null;
        }
        String color = LightsGameService.colorGame();
        Purifier purifier = getPurifierNoSchedule();
        for (int i = 0 ; i < 10; i ++)
        {
            purifier.setHex(color);
            purifierRepository.save(purifier);
            System.out.println("Purifier lights game on!" + color);
            color = LightsGameService.colorGame();
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
        if(!getPurifier().is_on()){
            return null;
        }
        // get parameters values from Air Quality Api
        Purifier purifier = getPurifierNoSchedule();
        String city = purifier.getLocation_name();
        String co = ApiService.GetParameter(city, "co");
        String no2 =ApiService.GetParameter(city, "no2");
        String o3 =ApiService.GetParameter(city, "o3");
        String so2 = ApiService.GetParameter(city, "so2") ;
        String msg = "CO: " + co + "; \n" + "NO2: " + no2 + "; \n" + "O3: " + o3 + "\n" + "SO2: " + so2 + "; \n";

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

        //Check param values to notify the user if the value is above average
        triggerAlarmAndMessage(Float.parseFloat(co), Float.parseFloat(no2), Float.parseFloat(o3), Float.parseFloat(so2));
        return msg;
    }

    public void addStatistics(Statistics statistic) {
        statistic.setPurifier_id(getPurifierNoSchedule().getPurifier_id());
        statisticsRepository.save(statistic);
    }

}



// http://localhost:8080/swagger-ui.html
            