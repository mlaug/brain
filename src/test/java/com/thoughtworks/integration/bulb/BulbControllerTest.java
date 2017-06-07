package com.thoughtworks.integration.bulb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.bulb.domain.Bulb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integration")
public class BulbControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldBeAbleToCreateNewBulbForUser() throws Exception {
        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("my bulb")))
                .andExpect(jsonPath("$.uuid", is("123-123-123")))
                .andExpect(jsonPath("$.userId", is("userid")))
                .andExpect(jsonPath("$.summary", is("this is my summary")));
    }

    @Test
    public void shouldBeAbleToUpdateBulbForUser() throws Exception {

        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("my bulb")))
                .andExpect(jsonPath("$.userId", is("userid")))
                .andExpect(jsonPath("$.summary", is("this is my summary")))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Bulb bulb = mapper.readValue(result.getResponse().getContentAsByteArray(), Bulb.class);

        String jsonToChange = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my changed title\",\n" +
                "  \"summary\" : \"my changed summary\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content(jsonToChange)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bulb.getId().intValue())))
                .andExpect(jsonPath("$.uuid", is("123-123-123")))
                .andExpect(jsonPath("$.title", is("my changed title")))
                .andExpect(jsonPath("$.summary", is("my changed summary")));
    }

    @Test
    public void shouldNotBeAbleToUpdateBulbsOfDifferentUsers() throws Exception {

        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.put("/foreignUserId/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        String jsonToChange = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my changed title\",\n" +
                "  \"summary\" : \"my changed summary\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content(jsonToChange)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldBeAbleToLinkTwoBulbsOfAUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content("{\n" +
                "  \"uuid\" : \"1\",\n" +
                "  \"title\" : \"my changed title\",\n" +
                "  \"summary\" : \"my changed summary\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content("{\n" +
                "  \"uuid\" : \"2\",\n" +
                "  \"title\" : \"my changed title\",\n" +
                "  \"summary\" : \"my changed summary\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        String json = "{\n" +
                "  \"link\" : \"2\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs/1").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldNotBeAbleToLinkTwoBulbsOfDifferentUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content("{\n" +
                "  \"uuid\" : \"1\",\n" +
                "  \"title\" : \"my changed title\",\n" +
                "  \"summary\" : \"my changed summary\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.put("/foreignId/bulbs").content("{\n" +
                "  \"uuid\" : \"2\",\n" +
                "  \"title\" : \"my changed title\",\n" +
                "  \"summary\" : \"my changed summary\"\n" +
                "}")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        String json = "{\n" +
                "  \"link\" : \"2\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs/1").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldBeAbleToDeleteBulbOfAUser() throws Exception {
        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("my bulb")))
                .andExpect(jsonPath("$.summary", is("this is my summary")))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        Bulb bulb = mapper.readValue(result.getResponse().getContentAsByteArray(), Bulb.class);

        mvc.perform(MockMvcRequestBuilders.get("/userid/bulbs/" + bulb.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("my bulb")))
                .andExpect(jsonPath("$.summary", is("this is my summary")));

        mvc.perform(MockMvcRequestBuilders.delete("/userid/bulbs/" + bulb.getUuid()))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders.get("/userid/bulbs/" + bulb.getUuid()))
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldNotBeAbleToDeleteBulbOfAnotherUser() throws Exception {
        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.put("/userid/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());


        mvc.perform(MockMvcRequestBuilders.delete("/foreignid/bulbs/123-123-123"))
                .andExpect(status().isUnauthorized());

    }


}
