package com.thoughtworks.bulb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
public class BulbLinkDto {

    @NonNull
    public String link;

}
