package com.thoughtworks.integration;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.repository.BulbRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BulbControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BulbRepository bulbRepository;

    @Test
    public void shouldBeAbleToCreateNewBulb() throws Exception {
        given(bulbRepository.save(Matchers.any(Bulb.class))).willAnswer(invocation -> {
            Bulb bulb = invocation.getArgumentAt(0, Bulb.class);
            return Bulb.builder()
                    .id(1L)
                    .summary(bulb.getSummary())
                    .title(bulb.getTitle())
                    .uuid("123-123-123")
                    .build();
        });
        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.post("/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("my bulb")))
                .andExpect(jsonPath("$.uuid", is("123-123-123")))
                .andExpect(jsonPath("$.summary", is("this is my summary")));

        verify(bulbRepository, never()).findOne(1L);
        verify(bulbRepository, never()).linkParentToChild(any(), any());
    }

    @Test
    public void shouldNotBeAbleToCreateNewBulbWithMissingInformation() throws Exception {
        String jsonWithoutSummary = "{\n" +
                "  \"title\" : \"my bulb\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.post("/bulbs").content(jsonWithoutSummary)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());


        String jsonWithoutTitle = "{\n" +
                "  \"summary\" : \"my bulb\"\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.post("/bulbs").content(jsonWithoutTitle)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldBeAbleToLinkBulbsWithEachOther() throws Exception {
        given(bulbRepository.save(any(Bulb.class))).willAnswer(invocation -> Bulb.builder()
                .summary("this is my summary")
                .title("my bulb")
                .uuid("123-123-123")
                .id(2L)
                .build());

        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\",\n" +
                "  \"parentBulbUuid\" : 1\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.post("/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(bulbRepository, times(1)).linkParentToChild("1", "123-123-123");
    }

    @Test
    public void shouldBeAbleToRetrieveAllCreatedBulbsAsTreeStructure() throws Exception {
        final Bulb parentBulb = Bulb.builder()
                .id(1L)
                .summary("Bulb")
                .title("samson")
                .uuid(UUID.randomUUID().toString())
                .children(Collections.singleton(
                        Bulb.builder()
                                .id(2L)
                                .title("Bulb Child")
                                .summary("child 1")
                                .uuid(UUID.randomUUID().toString())
                                .build()
                ))
                .build();

        given(bulbRepository.findOne(anyLong())).willAnswer(invocation -> parentBulb);
        given(bulbRepository.findRoots()).willAnswer(invocation -> asList(parentBulb));

        mvc.perform(MockMvcRequestBuilders.get(("/bulbs")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.children.length()", is(1)));
    }

    @Test
    public void shouldCreateBulbsIdempotentBasedOnUuid() throws Exception {

        String json = "{\n" +
                "  \"uuid\" : \"123-123-123\",\n" +
                "  \"title\" : \"my bulb\",\n" +
                "  \"summary\" : \"this is my summary\"\n" +
                "}";

        given(bulbRepository.save(Matchers.any(Bulb.class))).willAnswer(invocation -> {
            Bulb bulb = invocation.getArgumentAt(0, Bulb.class);
            return Bulb.builder()
                    .id(1L)
                    .summary(bulb.getSummary())
                    .title(bulb.getTitle())
                    .uuid("123-123-123")
                    .build();
        });

        mvc.perform(MockMvcRequestBuilders.post("/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        reset(bulbRepository);
        given(bulbRepository.findByUuid("123-123-123")).willAnswer(invocation -> Bulb.builder()
                .id(1L)
                .summary("this is my summary")
                .title("my bulb")
                .uuid("123-123-123")
                .build());

        given(bulbRepository.save(Matchers.any(Bulb.class))).willAnswer(invocation -> {
            Bulb bulb = invocation.getArgumentAt(0, Bulb.class);
            return Bulb.builder()
                    .id(2L) // this is the key part to make the test fail initially
                    .summary(bulb.getSummary())
                    .title(bulb.getTitle())
                    .uuid("123-123-123")
                    .build();
        });

        mvc.perform(MockMvcRequestBuilders.post("/bulbs").content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

    }


}
