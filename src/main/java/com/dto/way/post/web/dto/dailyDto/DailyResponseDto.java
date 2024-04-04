package com.dto.way.post.web.dto.dailyDto;

import lombok.*;

import java.time.LocalDateTime;

public class DailyResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDailyResultDto {
        private Long postId;
        private String title;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDailyResultDto {
        private Long postId;
        private String title;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDailyResultDto {
        private Long postId;
        private String title;
        private LocalDateTime deletedAt;
    }
}
