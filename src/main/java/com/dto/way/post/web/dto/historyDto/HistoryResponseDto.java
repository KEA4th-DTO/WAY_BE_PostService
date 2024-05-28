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
        private Boolean capture;
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
    public static class UpdateHistoryResultDto {
        private Long postId;
        private String title;
        private LocalDateTime updateAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHistoryResultDto {
        private Long postId;
        private String writerNickname;
        private String writerProfileImageUrl;
        private String title;
        private String body;
        private String bodyPreview;
        private Boolean isLiked;
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
