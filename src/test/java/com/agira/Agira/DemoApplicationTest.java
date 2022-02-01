package com.agira.Agira;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
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
import java.sql.Date;
import java.sql.Time;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@RunWith(SpringRunner.class)
@WebMvcTest(value = DemoApplication.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class DemoApplicationTest {

    private User user;
    private Purifier purifier;
    private Schedule schedule1;

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

        purifier = new Purifier();
        purifier.setPurifier_id(12);
        purifier.setAudio_id(2);
        purifier.setHex("#d8f05a");
        purifier.setIs_on(true);
        purifier.setLocation_name("Bucharest");
        purifier.setMessage_id(2);
        purifier.setSchedule_id(1);

        schedule1 = new Schedule();
        schedule1.setSchedule_id(1);
        schedule1.setEnd_time(new Time(23, 59, 59));
        schedule1.setStart_time(new Time(0, 0, 1));

        when(userRepo.findByUsername("radu6")).thenReturn(user);
        when(purifierRepository.findById(12)).thenReturn(purifier);
        when(scheduleRepository.findById(1)).thenReturn(schedule1);
    }


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
    private DataSource dataSource;

//    @TestConfiguration
//    static class TestConfig {
//
//        @Bean
//        public UserRepository userRepository() {
//            return new UserRepository() {
//                @Override
//                public List<User> findAll() {
//                    return null;
//                }
//
//                @Override
//                public List<User> findAll(Sort sort) {
//                    return null;
//                }
//
//                @Override
//                public Page<User> findAll(Pageable pageable) {
//                    return null;
//                }
//
//                @Override
//                public List<User> findAllById(Iterable<Long> longs) {
//                    return null;
//                }
//
//                @Override
//                public long count() {
//                    return 0;
//                }
//
//                @Override
//                public void deleteById(Long aLong) {
//
//                }
//
//                @Override
//                public void delete(User entity) {
//
//                }
//
//                @Override
//                public void deleteAllById(Iterable<? extends Long> longs) {
//
//                }
//
//                @Override
//                public void deleteAll(Iterable<? extends User> entities) {
//
//                }
//
//                @Override
//                public void deleteAll() {
//
//                }
//
//                @Override
//                public <S extends User> S save(S entity) {
//                    return null;
//                }
//
//                @Override
//                public <S extends User> List<S> saveAll(Iterable<S> entities) {
//                    return null;
//                }
//
//                @Override
//                public Optional<User> findById(Long aLong) {
//                    return Optional.empty();
//                }
//
//                @Override
//                public boolean existsById(Long aLong) {
//                    return false;
//                }
//
//                @Override
//                public void flush() {
//
//                }
//
//                @Override
//                public <S extends User> S saveAndFlush(S entity) {
//                    return null;
//                }
//
//                @Override
//                public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
//                    return null;
//                }
//
//                @Override
//                public void deleteAllInBatch(Iterable<User> entities) {
//
//                }
//
//                @Override
//                public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//                }
//
//                @Override
//                public void deleteAllInBatch() {
//
//                }
//
//                @Override
//                public User getOne(Long aLong) {
//                    return null;
//                }
//
//                @Override
//                public User getById(Long aLong) {
//                    return null;
//                }
//
//                @Override
//                public <S extends User> Optional<S> findOne(Example<S> example) {
//                    return Optional.empty();
//                }
//
//                @Override
//                public <S extends User> List<S> findAll(Example<S> example) {
//                    return null;
//                }
//
//                @Override
//                public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
//                    return null;
//                }
//
//                @Override
//                public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
//                    return null;
//                }
//
//                @Override
//                public <S extends User> long count(Example<S> example) {
//                    return 0;
//                }
//
//                @Override
//                public <S extends User> boolean exists(Example<S> example) {
//                    return false;
//                }
//
//                @Override
//                @Query("SELECT u FROM User u WHERE u.username = ?1")
//                public User findByUsername(String username) {
//                    User user = new User();
//                    user.setUsername("radu6");
//                    user.setPassword("123456");
//                    user.setDate_of_birth(new Date(2000));
//                    user.setAfflictions("diabet");
//                    user.setPurifier_id(12);
//                    return user;
//                }
//            };
//        }
//    }

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
    @WithMockUser(username = "radu6", password = "123456")
    void pickOnSchedule() throws Exception {

//        when(userRepo.findByUsername("radu6")).thenReturn(null);
//
//        // create request
//        RequestBuilder request = MockMvcRequestBuilders.put("/pickOnSchedule");
//        MvcResult result = mvc.perform(request).andReturn();
//
//        System.out.println(result.getResponse());
//        verify(userRepo).findByUsername("radu6");


//        String authStr = "radu6:" + encodedPassword;
////        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
////
////        // create headers
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("Authorization", "Basic " + base64Creds);
//
//        // Concatenate customer key and customer secret and use base64 to encode the concatenated string
//        String plainCredentials = "radu6" + ":" + "123456";
//        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
//        // Create authorization header
//        String authorizationHeader = "Basic " + base64Credentials;
        setup();

        System.out.println(userRepo.findByUsername("radu6"));
        System.out.println(userRepo.findAll());

        // create request
        RequestBuilder request = MockMvcRequestBuilders.put("/pickOnSchedule");
        MvcResult result = mvc.perform(request).andReturn();

        assertEquals("Your purifier is on!", result.getResponse().getContentAsString());
    }
}