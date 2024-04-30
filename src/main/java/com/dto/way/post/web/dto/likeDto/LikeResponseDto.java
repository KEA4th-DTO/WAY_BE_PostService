package com.dto.way.post.web.dto.likeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LikeResultDto {
        private Long postId;
        private Long likesCount;
    }


}
