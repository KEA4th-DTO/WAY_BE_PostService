package com.dto.way.post.web.dto.postDto;

import com.dto.way.post.domain.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostResultDto {
        private Long id;
        private Long memberId;
        private Double latitude;
        private Double longitude;
        private String title;
        private String body;
        private String imageUrl;
        private LocalDateTime expiredAt;
        private PostType postType;
        //TODO: 생성일자 추가하기
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostListResultDto {
        private List<GetPostResultDto> postResultDtoList;
    }

}
