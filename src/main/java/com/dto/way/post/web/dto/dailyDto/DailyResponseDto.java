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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDailyResultDto { //  작성자 정보(이름, 프로필 사진)을 여기서 가져와야할까?
        private String title;
        private String body;
        private String imageUrl;
        private LocalDateTime expiredAt;
        private LocalDateTime createdAt;

    }

}