package com.thoughtworks.integration.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.repository.BulbRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BulbRepositoryTest {

    @Autowired
    private BulbRepository bulbRepositoryToTest;

    // TODO: this is not working right now, shall we
    // consider the integration test to be run with a neo4j
    // or better to use an embedded one?
    @Before
    public void setUp() {

    }

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
        throw new NotImplementedException();
    }

    @Test
    public void shouldBeAbleToRetrieveAllRootNodes() {
        List<Bulb> rootNodes = bulbRepositoryToTest.findRoots();
        // TODO
    }
}
