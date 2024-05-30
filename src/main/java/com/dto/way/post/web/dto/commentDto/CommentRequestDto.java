package com.dto.way.post.web.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequestDto {

    @Getter
    public static class CreateCommentDto {
        @NotBlank(message = "댓글을 입력해주세요.")
        @Size(max = 200, message = "댓글의 최대 길이는 200자 입니다.")
        private String body;
    }

    @Getter
    public static class UpdateCommentDto {
        @NotBlank(message = "댓글을 입력해주세요.")
        @Size(max = 200, message = "댓글의 최대 길이는 200자 입니다.")
        private String body;
    }
}
