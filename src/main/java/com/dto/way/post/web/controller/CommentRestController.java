package com.dto.way.post.web.controller;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.commentService.CommentCommandService;
import com.dto.way.post.service.commentService.CommentQueryService;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

    @Operation(summary = "댓글 생성 API", description = "History 게시글에 댓글을 작성하는 API 입니다. PathVariable 으로 게시글의 postId, RequestBody 으로 댓글 내용을 전송해주세요.")
    @PostMapping("/{postId}")
    public ApiResponse<CommentResponseDto.CreateCommentResultDto> createComment(Authentication auth,
                                                                                @PathVariable(name = "postId") Long postId,
                                                                                @Valid @RequestBody CommentRequestDto.CreateCommentDto request) {
        Comment comment = commentCommandService.createComment(auth, postId, request);
        return ApiResponse.of(SuccessStatus.COMMENT_CREATED, CommentConverter.toCreateCommentResultDto(comment));
    }

    @Operation(summary = "댓글 삭제 API", description = "History 게시글에 댓글을 삭제하는 API 입니다. PathVariable 으로 삭제할 댓글의 commentId를 전송해주세요.")
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.DeleteCommentResultDto> createComment(Authentication auth,
                                                                                @PathVariable(name = "commentId") Long commentId) {
        return ApiResponse.of(SuccessStatus.COMMENT_DELETED, commentCommandService.deleteComment(auth, commentId));
    }

    @Operation(summary = "댓글 수정 API", description = "History 게시글에 댓글을 수정하는 API 입니다. PathVariable 으로 수정할 댓글의 commentId, RequestBody 으로 댓글 수정 내용을 전송해주세요.")
    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.UpdateCommentResultDto> updateComment(Authentication auth,
                                                                                @PathVariable(name = "commentId") Long commentId,
                                                                                @Valid @RequestBody CommentRequestDto.UpdateCommentDto request) {
        Comment comment = commentCommandService.updateComment(auth, commentId, request);
        return ApiResponse.of(SuccessStatus.COMMENT_UPDATED, CommentConverter.toUpdateCommentResultDto(comment));
    }

    @Operation(summary = "댓글 상세 조회(단건) API", description = "댓글을 상세 조회하는 API 입니다. PathVariable 으로 댓글의 commentId 를 전송해주세요.")
    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.GetCommentResultDto> getComment(Authentication auth,
                                                                          @PathVariable(name = "commentId") Long commentId) {
        Comment comment = commentQueryService.getComment(commentId);
        String loginMemberEmail = auth.getName();
        return ApiResponse.of(SuccessStatus.COMMENT_FOUND, CommentConverter.toGetCommentResultDto(loginMemberEmail, comment));
    }

    @Operation(summary = "게시글의 댓글 목록 조회 API", description = "게시글의 댓글 목록을 조회하는 API 입니다. PathVariable 으로 댓글 목록을 조회할 게시글의 postId 를 전송해주세요.")
    @GetMapping("/list/{postId}")
    public ApiResponse<CommentResponseDto.GetCommentListResultDto> getCommentList(Authentication auth,
                                                                                  @PathVariable(name = "postId") Long postId) {

        List<Comment> commentList = commentQueryService.getCommentList(postId);
        String loginMemberEmail = auth.getName();

        return ApiResponse.of(SuccessStatus.COMMENT_LIST_FOUND, CommentConverter.toGetCommentListResultDto(loginMemberEmail, commentList));
    }
}
