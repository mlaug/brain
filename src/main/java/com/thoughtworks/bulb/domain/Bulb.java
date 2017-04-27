package com.thoughtworks.bulb.domain;

import lombok.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bulb {

    @GraphId
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String summary;

    @NonNull
    private String uuid;

    @Relationship(type = "PARENT_OF", direction = Relationship.OUTGOING)
    private Set<Bulb> children = new HashSet<>();

}
