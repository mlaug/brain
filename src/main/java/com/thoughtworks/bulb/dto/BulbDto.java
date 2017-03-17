package com.thoughtworks.bulb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class BulbDto {

    @NonNull
    private String title;

    @NonNull
    private String summary;

}
