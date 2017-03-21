package com.thoughtworks.reference.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Getter
@Builder
public class Reference {

    @GraphId
    private Long id;

    @NonNull
    private String uuid;

    @NonNull
    private String title;

    @NonNull
    private String stereotype;

}
