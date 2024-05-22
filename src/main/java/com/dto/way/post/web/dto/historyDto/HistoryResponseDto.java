package com.dto.way.post.web.dto.historyDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHistoryResultDto {
        private Long postId;
        private String memberEmail;
        private String title;
        private String bodyHtmlUrl;
        private Long likesCount;
        private Long commentsCount;
        private Boolean isOwned;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHistoryListResultDto {
        private List<GetHistoryResultDto> historyResultDtoList;
    }

}
