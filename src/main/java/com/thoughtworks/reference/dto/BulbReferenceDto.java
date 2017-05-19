package com.thoughtworks.reference.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties
public class BulbReferenceDto {

    @NonNull
    public String reference;

    @NonNull
    public String uuid;

}
