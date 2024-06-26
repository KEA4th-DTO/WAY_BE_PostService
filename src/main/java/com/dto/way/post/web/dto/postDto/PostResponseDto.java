package com.dto.way.post.web.dto.postDto;

import com.dto.way.post.domain.enums.Expiration;
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
        private String writerNickname;
        private String writerProfileImageUrl;
        private String title;
        private String imageUrl;
        private PostType postType;
        private Boolean isLiked;
        private Long likesCount;
        private Long commentsCount;
        private String bodyPreview;
        private LocalDateTime createdAt;
        private LocalDateTime expiredAt;
        private Expiration postStatus;
        private Boolean inOwned;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostCountDto {
        private Long dailyCount;
        private Long historyCount;
    }

}