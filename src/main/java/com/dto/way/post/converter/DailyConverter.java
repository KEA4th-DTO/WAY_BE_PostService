package com.dto.way.post.converter;

import com.dto.way.post.domain.Daily;
import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.dailyDto.DailyRequestDto;
import com.dto.way.post.web.dto.dailyDto.DailyResponseDto;

import java.time.LocalDateTime;

public class DailyConverter {
    public static Daily toDaily(String imageUrl, DailyRequestDto.CreateDailyDto request) {
        Post postRequest = Post.builder()
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .build();

        return Daily.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .imageUrl(imageUrl)
                .expiredAt(request.getExpiredAt())
                .post(postRequest)
                .build();
    }
    public static DailyResponseDto.CreateDailyResultDto toCreateDailyResultDto(Daily daily) {
        return DailyResponseDto.CreateDailyResultDto.builder()
                .postId(daily.getPostId())
                .title(daily.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static DailyResponseDto.UpdateDailyResultDto toUpdateDailyResponseDto(Daily daily) {
        return DailyResponseDto.UpdateDailyResultDto.builder()
                .postId(daily.getPostId())
                .title(daily.getTitle())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static DailyResponseDto.DeleteDailyResultDto toDeleteDailyResponseDto(Daily daily) {
        return DailyResponseDto.DeleteDailyResultDto.builder()
                .postId(daily.getPostId())
                .title(daily.getTitle())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static DailyResponseDto.GetDailyResultDto toGetDailyResponseDto(Daily daily) {
        return DailyResponseDto.GetDailyResultDto.builder()
                .title(daily.getTitle())
                .body(daily.getBody())
                .imageUrl(daily.getImageUrl())
                .expiredAt(daily.getExpiredAt())
                .createdAt(daily.getPost().getCreatedAt())
                .build();
    }

}
