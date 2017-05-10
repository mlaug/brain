package com.thoughtworks.pact.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import org.apache.http.entity.ContentType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("integration")
public class BulbConsumerContractTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule(
            "bulb",
            "localhost",
            9000,
            this
    );

    @Pact(provider = "bulb", consumer = "some-consumer")
    public PactFragment noBulbAvailable(PactDslWithProvider builder) {
        return builder
                .given("no bulbs yet available")
                .uponReceiving("Empty list of bulbs")
                .path("/bulbs")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("[]", MediaType.APPLICATION_JSON_UTF8_VALUE)
                .toFragment();
    }

    @Test
    @PactVerification(value = "bulb",fragment = "noBulbAvailable")
    public void shouldGetAnEmptyListOfBulbs() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        List list = restTemplate.getForObject("http://localhost:9000/bulbs", List.class);
        assertThat(list.size(), is(0));
    }

    @Pact(provider = "bulb", consumer = "some-consumer")
    public PactFragment oneBasicBulbAvailable(PactDslWithProvider builder) {
        return builder
                .given("one basic bulb available")
                .uponReceiving("One basic bulb")
                .path("/bulbs")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("[{\n" +
                        "  \"title\": \"my bulb\",\n" +
                        "  \"summary\": \"this is my summary\"\n" +
                        "}]", ContentType.APPLICATION_JSON)
                .toFragment();
    }

    @Test
    @PactVerification(value = "bulb",fragment = "oneBasicBulbAvailable")
    public void shouldGetOneBasicBulb() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        List list = restTemplate.getForObject("http://localhost:9000/bulbs", List.class);
        assertThat(list.size(), is(1));
    }

}
