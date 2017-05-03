package com.thoughtworks.bulb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
public class BulbDto {

    private String title;

    private String summary;

    @NonNull
    private String uuid;

}
