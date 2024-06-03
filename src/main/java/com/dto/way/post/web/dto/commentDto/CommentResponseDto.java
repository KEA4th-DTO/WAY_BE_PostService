package com.dto.way.post.web.dto.commentDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCommentResultDto{
        private Long commentId;
        private LocalDateTime createAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteCommentResultDto{
        private Long commentId;
        private LocalDateTime deletedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCommentResultDto{
        private Long commentId;
        private LocalDateTime updatedAt;
    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCommentResultDto{
        private Long commentId;
        private String writerNickname;
        private String writerProfileImageUrl;
        private String body;
        private Long replyCounts;
        private Boolean isOwned;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCommentListResultDto{
        private List<GetCommentResultDto> commentResultDtoList;
    }

}
