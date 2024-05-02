package com.dto.way.post.web.controller;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.CommentService.CommentCommandService;
import com.dto.way.post.service.CommentService.CommentQueryService;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-service/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/{postId}")
    public ApiResponse<CommentResponseDto.CreateCommentResultDto> createComment(Authentication auth,
                                                                                @PathVariable(name = "postId") Long postId,
                                                                                @RequestBody CommentRequestDto.CreateCommentDto request) {
        Comment comment = commentCommandService.createComment(auth, postId, request);
        return ApiResponse.of(SuccessStatus.COMMENT_CREATED, CommentConverter.toCreateCommentResultDto(comment));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.DeleteCommentResultDto> createComment(Authentication auth,
                                                                                @PathVariable(name = "commentId") Long commentId) {
        return ApiResponse.of(SuccessStatus.COMMENT_DELETED, commentCommandService.deleteComment(auth, commentId));
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.UpdateCommentResultDto> updateComment(Authentication auth,
                                                                         @PathVariable(name = "commentId") Long commentId,
                                                                         @RequestBody CommentRequestDto.UpdateCommentDto request) {
        Comment comment = commentCommandService.updateComment(auth, commentId, request);
        return ApiResponse.of(SuccessStatus.COMMENT_UPDATED, CommentConverter.toUpdateCommentResultDto(comment));
    }

    @GetMapping("/list/{postId}")
    public ApiResponse<CommentResponseDto.GetCommentListResultDto> getCommentList(@PathVariable(name = "postId") Long postId) {
        List<Comment> commentList = commentQueryService.getCommentList(postId);

        return ApiResponse.of(SuccessStatus.COMMENT_LIST_FOUND, CommentConverter.toGetCommentListResultDto(commentList));
    }
}
