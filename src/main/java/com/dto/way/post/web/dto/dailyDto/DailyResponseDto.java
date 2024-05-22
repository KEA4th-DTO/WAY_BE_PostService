package com.dto.way.post.web.dto.dailyDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDailyResultDto {
        private Long postId;
        private String writerEmail;
        private String title;
        private String body;
        private String imageUrl;
        private Boolean isOwned;
        private LocalDateTime expiredAt;
        private LocalDateTime createdAt;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetDailyListResultDto {

        private List<GetDailyResultDto> dailyResultDtoList;
    }
}
