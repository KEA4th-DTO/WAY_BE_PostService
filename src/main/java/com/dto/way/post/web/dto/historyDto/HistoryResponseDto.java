package com.dto.way.post.web.dto.historyDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class HistoryResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateHistoryResultDto {
        private Long postId;
        private String title;
        private LocalDateTime createAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteHistoryResultDto {
        private Long postId;
        private String title;
        private LocalDateTime deletedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHistoryResultDto {
        private Long postId;
        private String memberEmail;
        private String title;
        private String bodyHtmlUrl;
        private Long commentsCount;
        private LocalDateTime createdAt;
    }
}
