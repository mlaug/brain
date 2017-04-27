package com.thoughtworks.integration;

import com.thoughtworks.reference.repository.ReferenceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReferenceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReferenceRepository referenceRepository;

    @Test
    public void shouldBeAbleToCreateReference() throws Exception {
        //language=JSON
        String json = "{\n" +
                "  \"title\" : \"my reference\",\n" +
                "  \"stereotype\" : \"book\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.post("/reference").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referenceId", notNullValue()))
                .andExpect(jsonPath("$.destination", notNullValue()));
    }

    @Test
    public void shouldNotBeAbleToCreateReferenceWithInvalidData() throws Exception {
        //language=JSON
        String jsonWithoutStereotype = "{\n" +
                "  \"title\" : \"my reference\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.post("/reference").content(jsonWithoutStereotype)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

        //language=JSON
        String jsonWithoutTitle = "{\n" +
                "  \"stereotype\" : \"my reference\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.post("/reference").content(jsonWithoutTitle)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());

    }

}