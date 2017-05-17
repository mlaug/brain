package com.thoughtworks.reference.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
public class BulbReferenceDto {

    @NonNull
    public String reference;

}
