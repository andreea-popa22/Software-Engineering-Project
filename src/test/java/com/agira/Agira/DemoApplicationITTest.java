package com.agira.Agira;

import com.agira.Agira.Entities.*;
import com.agira.Agira.Repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DemoApplication.class
)
@AutoConfigureMockMvc
public class DemoApplicationITTest {
    @Before
    public void setup(){
        Purifier purifier = new Purifier();
        purifier.setIs_on(false);
        purifier.setLocation_name("Bucharest");
        purifier.setSchedule_id(2);
        purifierRepository.save(purifier);

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
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
        scheduleRepository.save(schedule4);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurifierRepository purifierRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AudioRepository audioRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Test
    @WithMockUser(username = "radu8", password = "123456")
    public void test1() throws Exception {
        //create profile
        User user = new User();
        user.setUsername("radu8");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("123456");
        user.setPassword(encodedPassword);
        user.setDate_of_birth(new Date(2000));
        user.setAfflictions("astm");
        user.setPurifier_id(1);

        setup();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);

        RequestBuilder request = MockMvcRequestBuilders.post("/process_register")
                .contentType(APPLICATION_JSON)
                .content(requestJson);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        User u = new ObjectMapper().readValue(json, User.class);
        assertEquals(user.getPurifier_id(), u.getPurifier_id());
        assertEquals(user.getUsername(), u.getUsername());

        //edit profile
        User user2 = new User();
        user2.setDate_of_birth(new Date(1999));
        user2.setAfflictions("difterie");

        requestJson = ow.writeValueAsString(user2);
        request = MockMvcRequestBuilders.post("/process_edit_profile")
                .contentType(APPLICATION_JSON)
                .content(requestJson);
        result = mockMvc.perform(request).andReturn();
        json = result.getResponse().getContentAsString();
        u = new ObjectMapper().readValue(json, User.class);
        assertEquals(user2.getAfflictions(), u.getAfflictions());

        assertFalse(purifierRepository.findById(1).is_on());

        //Turn purifier on
        request = MockMvcRequestBuilders.put("/pickOnSchedule");
        mockMvc.perform(request).andExpect(status().isOk());

        assertTrue(purifierRepository.findById(1).is_on());

        //Turn on lights game
        request = MockMvcRequestBuilders.put("/turnOnLightsGame");
        result = mockMvc.perform(request).andReturn();

        assertEquals("Purifier lights game on!", result.getResponse().getContentAsString());

        //Edit Location
        request = MockMvcRequestBuilders.put("/editPurifierLocation")
                .param("location", "London");
        result = mockMvc.perform(request).andReturn();
        json = result.getResponse().getContentAsString();
        json = json.replaceAll("_on", "is_on");
        Purifier p = new ObjectMapper().readValue(json, Purifier.class);
        assertEquals("London", p.getLocation_name());

        //Get Purifier State
        request = MockMvcRequestBuilders.get("/getPurifierState");
        result = mockMvc.perform(request).andReturn();
        json = result.getResponse().getContentAsString();
        json = json.replaceAll("_on", "is_on");
        Purifier purifierResult = new ObjectMapper().readValue(json, Purifier.class);
        assertEquals(purifierRepository.findById(1), purifierResult);
    }
}