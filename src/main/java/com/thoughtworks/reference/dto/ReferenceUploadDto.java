package com.thoughtworks.reference.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ReferenceUploadDto {

    @NonNull
    private String referenceId;

    @NonNull
    private String destination;

}
