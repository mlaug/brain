package com.thoughtworks.reference.domain;

import lombok.*;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulbReference {

    @GraphId
    private Long id;

    @NonNull
    private String reference;

    @NonNull
    private String uuid;

}
