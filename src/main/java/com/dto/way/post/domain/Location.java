package com.dto.way.post.domain;

import lombok.Getter;

@Getter
public class Location {

    private final Double latitude;
    private final Double longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}