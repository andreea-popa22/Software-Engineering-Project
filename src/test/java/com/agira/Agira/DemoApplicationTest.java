package com.agira.Agira;

import com.agira.Agira.Entities.*;
import com.agira.Agira.Repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import javax.sql.DataSource;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = DemoApplication.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class DemoApplicationTest {
    private Purifier purifier;
    private User user;
    private User user2;
    private Purifier purifier3;

    @Before
    public void setup() {
        user = new User();
        user.setUsername("radu6");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("123456");
        user.setPassword(encodedPassword);
        user.setDate_of_birth(new Date(2000));
        user.setAfflictions("diabet");
        user.setPurifier_id(12);

        user2 = new User();
        user2.setUsername("radu7");
        user2.setPassword("1234567");
        user2.setDate_of_birth(new Date(2001));
        user2.setAfflictions("astm");
        user2.setPurifier_id(13);

        purifier = new Purifier();
        purifier.setPurifier_id(12);
        purifier.setAudio_id(2);
        purifier.setHex("#d8f05a");
        purifier.setIs_on(true);
        purifier.setLocation_name("Bucharest");
        purifier.setMessage_id(2);
        purifier.setSchedule_id(1);

        purifier3 = new Purifier();
        purifier3.setPurifier_id(13);
        purifier3.setAudio_id(1);
        purifier3.setHex("#d8f12");
        purifier3.setIs_on(true);
        purifier3.setLocation_name("Paris");
        purifier3.setMessage_id(1);
        purifier3.setSchedule_id(1);

        purifier2.setPurifier_id(12);
        purifier2.setAudio_id(2);
        purifier2.setHex("#d8f05a");
        purifier2.setIs_on(true);
        purifier2.setLocation_name("Bucharest");
        purifier2.setMessage_id(2);
        purifier2.setSchedule_id(1);

        Schedule schedule1 = new Schedule();
        schedule1.setSchedule_id(1);
        schedule1.setEnd_time(new Time(23, 59, 59));
        schedule1.setStart_time(new Time(0, 0, 1));
        Schedule schedule2 = new Schedule();
        schedule2.setSchedule_id(2);
        schedule2.setEnd_time(new Time(0, 0, 0));
        schedule2.setStart_time(new Time(0, 0, 0));
        Schedule schedule3 = new Schedule();
        schedule3.setSchedule_id(3);
        schedule3.setEnd_time(new Time(7, 0, 0));
        schedule3.setStart_time(new Time(19, 0, 0));
        Schedule schedule4 = new Schedule();
        schedule4.setSchedule_id(4);
        schedule4.setEnd_time(new Time(19, 0, 0));
        schedule4.setStart_time(new Time(7, 0, 0));

        Audio audio = new Audio();
        audio.setAudio_id(1);
        audio.setAudio_file("a1.mp3");

        Message message = new Message();
        message.setMessage_id(1);
        message.setMessage_text("nivel periculos");


        when(userRepo.findByUsername("radu6")).thenReturn(user);
        when(userRepo.findByUsername("radu7")).thenReturn(user2);
        when(purifierRepository.findById(12)).thenReturn(purifier);
        when(scheduleRepository.findById(1)).thenReturn(schedule1);
        when(scheduleRepository.findById(2)).thenReturn(schedule2);
        when(scheduleRepository.findById(3)).thenReturn(schedule3);
        when(scheduleRepository.findById(4)).thenReturn(schedule4);
        when(audioRepository.findById(1)).thenReturn(audio);
        when(messageRepository.findById(1)).thenReturn(message);
    }

    @Autowired
    private DemoApplication demoApplication;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private PurifierRepository purifierRepository;

    @MockBean
    private AudioRepository audioRepository;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private StatisticsRepository statisticsRepository;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private Purifier purifier2;


    private String color;
    private Integer Id;


    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void getUser() throws Exception {
        setup();
        User result = demoApplication.getUser();
        verify(userRepo, times(1)).findByUsername("radu6");
        assertEquals(user, result);
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void getPurifierState() throws Exception {
        setup();

        RequestBuilder request = MockMvcRequestBuilders.get("/getPurifierState");
        MvcResult result = mvc.perform(request).andReturn();

        String json = result.getResponse().getContentAsString();
        json = json.replaceAll("_on", "is_on");
        Purifier purifierResult = new ObjectMapper().readValue(json, Purifier.class);
        assertEquals(purifier, purifierResult);
    }

    @ParameterizedTest
    @CsvSource({ "1", "2" ,"3", "4"})
    @WithMockUser(username = "radu6", password = "123456")
    void pickSchedule(int n) throws Exception{
        setup();
        demoApplication.pickSchedule(n);
        System.out.println(purifier.getSchedule_id());
        assertEquals(n, purifier.getSchedule_id());
    }


    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void pickOnSchedule() throws Exception {
        setup();
        // create request
        RequestBuilder request = MockMvcRequestBuilders.put("/pickOnSchedule");
        MvcResult result = mvc.perform(request).andReturn();

        assertTrue(purifier.is_on());
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void pickOffSchedule() throws Exception {
        setup();
        // create request
        RequestBuilder request = MockMvcRequestBuilders.put("/pickOffSchedule");
        MvcResult result = mvc.perform(request).andReturn();
        System.out.println(purifier.is_on());
        assertFalse(purifier.is_on());
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void pickDaySchedule() throws Exception {
        setup();
        // create request
        RequestBuilder request = MockMvcRequestBuilders.put("/pickDaySchedule");
        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("Your purifier is on day schedule (7:00 - 19:00)!", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void pickNightSchedule() throws Exception {
        setup();
        // create request
        RequestBuilder request = MockMvcRequestBuilders.put("/pickNightSchedule");
        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("Your purifier is on night schedule (19:00 - 7:00)!", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void turnOnLightsGame() throws Exception{
        setup();
        RequestBuilder request = MockMvcRequestBuilders.put("/turnOnLightsGame");
        MvcResult result = mvc.perform(request).andReturn();

        verify(purifierRepository, times(10)).save(purifier);

        assertEquals("Purifier lights game on!", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void checkSchedule() throws Exception{
        setup();
        when(purifierRepository.findById(12)).thenReturn(purifier2);
        demoApplication.checkSchedule(purifier2);
        verify(purifier2).setIs_on(anyBoolean());
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void getPurifier() throws Exception{
        setup();
        Purifier p = demoApplication.getPurifier();
        if(demoApplication.checkSchedule(purifier)){
            assertEquals(purifier, p);
        }
        else {
            assertNull(p);
        }
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void getPurifierNoSchedule() throws Exception{
        setup();
        Purifier p = demoApplication.getPurifierNoSchedule();
        assertEquals(purifier, p);
    }


    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void processRegister() throws Exception {
        setup();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user2);

        RequestBuilder request = MockMvcRequestBuilders.post("/process_register")
                .contentType(APPLICATION_JSON)
                .content(requestJson);
        mvc.perform(request).andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void viewProfile() throws Exception {
        setup();

        RequestBuilder request = MockMvcRequestBuilders.get("/profile");
        MvcResult result = mvc.perform(request).andReturn();
        String json = result.getResponse().getContentAsString();
        User u = new ObjectMapper().readValue(json, User.class);
        assertEquals(user, u);
    }

    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void processEditProfile() throws Exception {
        setup();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user2);
        user2.setPassword(new BCryptPasswordEncoder().encode("123456"));
        RequestBuilder request = MockMvcRequestBuilders.post("/process_edit_profile")
                .contentType(APPLICATION_JSON)
                .content(requestJson);
        MvcResult result = mvc.perform(request).andReturn();
        String json = result.getResponse().getContentAsString();
        User u = new ObjectMapper().readValue(json, User.class);
        assertEquals(user2, u);
    }


    @Test
    @WithMockUser(username = "radu6", password = "123456")
    void editPurifierLocation() throws Exception {
        setup();
        RequestBuilder request = MockMvcRequestBuilders.put("/editPurifierLocation")
                .param("location", "London");
        MvcResult result = mvc.perform(request).andReturn();
        String json = result.getResponse().getContentAsString();
        json = json.replaceAll("_on", "is_on");
        Purifier p = new ObjectMapper().readValue(json, Purifier.class);
        assertEquals("London", p.getLocation_name());
    }


}