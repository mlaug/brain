package com.thoughtworks.reference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
public class ReferenceDto {

    @NonNull
    private String title;

    @NonNull
    private String stereotype;

}
