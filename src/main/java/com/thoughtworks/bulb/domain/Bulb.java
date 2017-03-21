package com.thoughtworks.bulb.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Builder
@Getter
public class Bulb {

    @GraphId
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String summary;

}
