package com.dto.way.post.converter;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;

import java.time.LocalDateTime;

public class DailyConverter {
    public static Daily toDaily(String imageUrl, DailyRequestDto.CreateDailyDto request) {
        return Daily.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .imageUrl(imageUrl)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .expiredAt(request.getExpiredAt())
                .build();
    }
    public static DailyResponseDto.CreateDailyResultDto toCreateDailyResultDto(Daily daily) {
        return DailyResponseDto.CreateDailyResultDto.builder()
                .id(daily.getId())
                .title(daily.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
