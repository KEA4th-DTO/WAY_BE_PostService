package com.dto.way.post.web.dto.dailyDto;

import lombok.*;

import java.time.LocalDateTime;

public class DailyResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDailyResultDto {
        private Long id;
        private String title;
        private LocalDateTime createdAt;
    }
}
