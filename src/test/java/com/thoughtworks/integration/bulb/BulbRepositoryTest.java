package com.thoughtworks.integration.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.repository.BulbRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class BulbRepositoryTest {

    @Autowired
    private BulbRepository bulbRepositoryToTest;

    @Test
    public void shouldBeAbleToStoreBulb() {
        Bulb bulb = bulbRepositoryToTest.save(Bulb.builder()
                .summary("samson")
                .title("samson")
                .uuid(UUID.randomUUID().toString())
                .build());
        assertThat(bulb.getId(), isA(Long.class));
    }

    @Test
    public void shouldBeAbleToLinkToBulbs() {

    }

    @Test
    public void shouldBeAbleToRetrieveAllRootNodes() {

    }
}
