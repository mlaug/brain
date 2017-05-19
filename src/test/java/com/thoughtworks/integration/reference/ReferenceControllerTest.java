package com.thoughtworks.integration.reference;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integration")
public class ReferenceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldBeAbleToAddReference() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/bulbs").content("{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        String json = "{\n" +
                "  \"reference\": \"https://en.wikipedia.org/wiki/Confused_deputy_problem\",\n" +
                "  \"uuid\": \"345-345-345\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.put("/bulbs/123-123-123/references").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.reference", is("https://en.wikipedia.org/wiki/Confused_deputy_problem")));

        mvc.perform(MockMvcRequestBuilders.get("/bulbs/123-123-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.references.length()", is(1)));
    }

}