package com.dto.way.post.web.dto.postDto;

import com.dto.way.post.domain.enums.PostType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostResultDto {
        private Long postId;
        private Long memberId;
        private String title;
        private String imageUrl;
        private PostType postType;
        private Long likesCount;
        //TODO: 생성일자 추가하기
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostListResultDto {
        private List<GetPostResultDto> postResultDtoList;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPinResultDto {
        private Long postId;
        private Double latitude;
        private Double longitude;

        @Enumerated(value = EnumType.STRING)
        private PostType postType;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPinListResultDto {
        private List<GetPinResultDto> pinResultDtoList;
    }

}