package com.thoughtworks.bulb.domain;

import lombok.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Builder
@Getter
// FIXME: can we do this without setters?
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bulb {

    @GraphId
    private Long id;

    private String title;

    private String summary;

    @NonNull
    @Index(unique = true)
    private String uuid;

}
