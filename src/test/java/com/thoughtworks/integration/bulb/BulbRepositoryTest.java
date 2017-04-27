package com.thoughtworks.integration.bulb;

import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.repository.BulbRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        Bulb parent = bulbRepositoryToTest.save(Bulb.builder()
                .summary("parent")
                .title("parent")
                .uuid(UUID.randomUUID().toString())
                .build());

        Bulb child = bulbRepositoryToTest.save(Bulb.builder()
                .summary("child")
                .title("child")
                .uuid(UUID.randomUUID().toString())
                .build());

        Bulb parentWithoutChild = bulbRepositoryToTest.findOne(parent.getId());
        bulbRepositoryToTest.linkParentToChild(parent.getUuid(), child.getUuid());
        Bulb parentWithChild = bulbRepositoryToTest.findOne(parent.getId());
        assertThat(parentWithChild.getChildren().size(), is(1));
    }

    @Test
    public void shouldBeAbleToRetrieveAllRootNodes() {
        List<Bulb> rootNodes = bulbRepositoryToTest.findRoots();
        // TODO
    }
}
