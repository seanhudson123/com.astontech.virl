package com.astontech.virl.student.controllers;


import com.astontech.virl.student.Application;
import com.astontech.virl.student.domain.Mentee;
import com.astontech.virl.student.repositories.MenteeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.stream.IntStream;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MenteeControllerTest {

    private String testName = "Mentee1";


    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                  MediaType.APPLICATION_JSON.getSubtype(),
                                                  Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private MenteeRepository menteeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        menteeRepository.deleteAll();

        generateTestMentees(7);
    }

    @Test
    public void readSingleByName() throws Exception {
        System.out.println("TESTING MENTEE BY NAME");
        mockMvc.perform(get("/api/mentee/name/" + testName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(testName)));
    }

    @Test
    public void failSingleByName() throws Exception {
        mockMvc.perform(get("/api/mentee/name/" + "NAME_THAT_CANT_BE_FOUND"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createMentee() throws Exception {
        Mentee jason = new Mentee();
        jason.setName("Jason");
        jason.setSite("CA");
        jason.setBu("Cisco");

        String jasonJson = json(jason);

        mockMvc.perform(post("/api/mentee/")
                .contentType(contentType)
                .content(jasonJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Jason")))
                .andExpect(jsonPath("$.site", is("CA")))
                .andExpect(jsonPath("$.bu", is("Cisco")));

    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    public void generateTestMentees(int numberOfMentees) {
        IntStream.range(1, numberOfMentees).forEach(i -> {
            Mentee mentee = new Mentee();
            mentee.setName("Mentee" + i);
            mentee.setBu("Cisco");
            mentee.setSite((i % 2 == 0) ? "CA" : "MN");
            menteeRepository.save(mentee);
        });
    }

}
