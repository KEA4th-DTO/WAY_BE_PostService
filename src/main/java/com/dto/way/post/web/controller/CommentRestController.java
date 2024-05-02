package com.dto.way.post.web.controller;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.CommentService.CommentCommandService;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post-service/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentCommandService commentCommandService;

    @PostMapping("/{postId}")
    public ApiResponse<CommentResponseDto.CreateCommentResultDto> createComment(Authentication auth,
                                                                                @PathVariable(name = "postId") Long postId,
                                                                                @RequestBody CommentRequestDto.CreateCommentDto request) {
        Comment comment = commentCommandService.createComment(auth, postId, request);
        return ApiResponse.of(SuccessStatus.COMMENT_CREATED, CommentConverter.toCreateCommentResultDto(comment));
    }
}