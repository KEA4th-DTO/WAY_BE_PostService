package com.dto.way.post.web.dto.dailyDto;

import lombok.Getter;

import java.time.LocalDateTime;

public class DailyRequestDto {

    @Getter
    public static class CreateDailyDto {
        private String title;
        private String body;
        private Double latitude;
        private Double longitude;
        private LocalDateTime expiredAt;
    }


}
