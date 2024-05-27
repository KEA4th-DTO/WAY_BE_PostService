package com.dto.way.post.web.dto.replyDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateReplyResultDto{
        private Long replyId;
        private LocalDateTime createAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteReplyResultDto{
        private Long replyId;
        private LocalDateTime deletedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateReplyResultDto{
        private Long replyId;
        private LocalDateTime updatedAt;
    }


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetReplyResultDto{
        private String writerNickname;
        private String writerProfileImageUrl;
        private String body;
        private LocalDateTime createdAt;
        private Boolean isOwned;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetReplyListResultDto{
        private List<ReplyResponseDto.GetReplyResultDto> replyResultDtoList;
    }

}




