package com.thoughtworks.integration.bulb;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.bulb.domain.Bulb;
import com.thoughtworks.bulb.repository.BulbRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BulbRepositoryRegressionTest {

    @Autowired
    private BulbRepository bulbRepositoryToTest;

    /**
     * version 4.2.1 of spring-data-neo4j has a bug
     * directions of a graph are being ignored and
     * always added to the @Relationship annotated field
     */
    @Test
    public void shouldNotThrowAnExceptionIfMoreThanOneChildElement() throws JsonProcessingException {
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

        Bulb anotherChild = bulbRepositoryToTest.save(Bulb.builder()
                .summary("child")
                .title("child")
                .uuid(UUID.randomUUID().toString())
                .build());

        bulbRepositoryToTest.linkParentToChild(parent.getUuid(), child.getUuid());
        bulbRepositoryToTest.linkParentToChild(parent.getUuid(), anotherChild.getUuid());

        Bulb parentWithChilds = bulbRepositoryToTest.findOne(parent.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        String parentAsString = objectMapper.writeValueAsString(parentWithChilds);
        assertThat(parentAsString, notNullValue());
    }
}
