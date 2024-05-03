package com.dto.way.post.web.dto.replyDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetReplyResultDto{
        private String memberEmail;
        private String body;
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetReplyListResultDto{
        private List<ReplyResponseDto.GetReplyResultDto> replyResultDtoList;
    }

}




