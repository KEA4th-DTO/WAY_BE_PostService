package com.dto.way.post.web.dto.commentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
