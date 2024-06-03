package com.dto.way.post.web.dto.commentLikeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentLikeResponseDto {


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentLikeResultDto {
        private Long commentId;
        private Long commentLikesCount;
    }

}
