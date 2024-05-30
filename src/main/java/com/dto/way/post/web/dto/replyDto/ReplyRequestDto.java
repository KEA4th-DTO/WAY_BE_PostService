package com.dto.way.post.web.dto.replyDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReplyRequestDto {

    @Getter
    public static class CreateReplyDto {
        @Size(max = 200, message = "대댓글의 최대 길이는 200자 입니다.")
        @NotBlank(message = "대댓글을 입력해주세요.")
        private String body;
    }

    @Getter
    public static class UpdateReplyDto {
        @Size(max = 200, message = "대댓글의 최대 길이는 200자 입니다.")
        @NotBlank(message = "대댓글을 입력해주세요.")
        private String body;
    }
}
