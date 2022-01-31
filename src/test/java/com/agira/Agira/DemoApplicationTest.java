package com.agira.Agira;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import java.net.URI;


import javax.sql.DataSource;

import java.net.http.HttpResponse;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = DemoApplication.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class DemoApplicationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PurifierRepository purifierRepository;

    @MockBean
    private AudioRepository audioRepository;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private DataSource dataSource;

    @Test
    void getPurifierState() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/getPurifierState");
        MvcResult result = mvc.perform(request).andReturn();
        Purifier p = new Purifier();
        p.setPurifier_id(12);
        p.setAudio_id(2);
        p.setHex("#d8f05a");
        p.setIs_on(true);
        p.setLocation_name("Bucharest");
        p.setMessage_id(2);
        p.setSchedule_id(1);
        String s = "{\n" +
                "  \"purifier_id\": 12,\n" +
                "  \"audio_id\": 2,\n" +
                "  \"message_id\": 2,\n" +
                "  \"collection_id\": 0,\n" +
                "  \"hex\": \"#d8f05a\",\n" +
                "  \"location_name\": \"Bucharest\",\n" +
                "  \"schedule_id\": 1,\n" +
                "  \"_on\": true\n" +
                "}";
        assertEquals(s, result.getResponse().getContentAsString());
    }

    @Test
    void pickOnSchedule() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("123456");
        String authStr = "radu6:" + encodedPassword;
//        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
//
//        // create headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Basic " + base64Creds);

        // Concatenate customer key and customer secret and use base64 to encode the concatenated string
        String plainCredentials = "radu6" + ":" + "123456";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        // Create authorization header
        String authorizationHeader = "Basic " + base64Credentials;

        // create request
        RequestBuilder request = MockMvcRequestBuilders.put("/pickOnSchedule");
        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("Your purifier is on!", result.getResponse().getContentAsString());
    }
}