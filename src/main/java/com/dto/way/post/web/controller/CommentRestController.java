package com.dto.way.post.web.controller;

import com.dto.way.message.NotificationMessage;
import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.service.commentLikeService.CommentLikeCommandService;
import com.dto.way.post.service.commentLikeService.CommentLikeQueryService;
import com.dto.way.post.service.commentService.CommentCommandService;
import com.dto.way.post.service.commentService.CommentQueryService;
import com.dto.way.post.service.notificationService.NotificationService;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import com.dto.way.post.web.dto.commentLikeDto.CommentLikeResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/post-service/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;
    private final CommentLikeCommandService commentLikeCommandService;
    private final CommentLikeQueryService commentLikeQueryService;
    private final NotificationService notificationService;
    private final JwtUtils jwtUtils;
    private final MemberClient memberClient;


    @Operation(summary = "댓글 생성 API", description = "게시글에 댓글을 작성하는 API 입니다. PathVariable 으로 게시글의 postId, RequestBody 으로 댓글 내용을 전송해주세요.")
    @PostMapping("/{postId}")
    public ApiResponse<CommentResponseDto.CreateCommentResultDto> createComment(HttpServletRequest httpServletRequest,
                                                                                @PathVariable(name = "postId") Long postId,
                                                                                @Valid @RequestBody CommentRequestDto.CreateCommentDto request) {
        Comment comment = commentCommandService.createComment(httpServletRequest, postId, request);


        return ApiResponse.of(SuccessStatus.COMMENT_CREATED, CommentConverter.toCreateCommentResultDto(comment));
    }

    @Operation(summary = "댓글 삭제 API", description = "게시글에 댓글을 삭제하는 API 입니다. PathVariable 으로 삭제할 댓글의 commentId를 전송해주세요.")
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.DeleteCommentResultDto> createComment(HttpServletRequest httpServletRequest,
                                                                                @PathVariable(name = "commentId") Long commentId) {
        return ApiResponse.of(SuccessStatus.COMMENT_DELETED, commentCommandService.deleteComment(httpServletRequest, commentId));
    }

    @Operation(summary = "댓글 수정 API", description = "게시글에 댓글을 수정하는 API 입니다. PathVariable 으로 수정할 댓글의 commentId, RequestBody 으로 댓글 수정 내용을 전송해주세요.")
    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.UpdateCommentResultDto> updateComment(HttpServletRequest httpServletRequest,
                                                                                @PathVariable(name = "commentId") Long commentId,
                                                                                @Valid @RequestBody CommentRequestDto.UpdateCommentDto request) {
        Comment comment = commentCommandService.updateComment(httpServletRequest, commentId, request);
        return ApiResponse.of(SuccessStatus.COMMENT_UPDATED, CommentConverter.toUpdateCommentResultDto(comment));
    }

    @Operation(summary = "댓글 상세 조회(단건) API", description = "댓글을 상세 조회하는 API 입니다. PathVariable 으로 댓글의 commentId 를 전송해주세요.")
    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponseDto.GetCommentResultDto> getComment(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "commentId") Long commentId) {
        CommentResponseDto.GetCommentResultDto getCommentResultDto = commentQueryService.getCommentResultDto(httpServletRequest, commentId);

        return ApiResponse.of(SuccessStatus.COMMENT_FOUND, getCommentResultDto);
    }

    @Operation(summary = "게시글의 댓글 목록 조회 API", description = "게시글의 댓글 목록을 조회하는 API 입니다. PathVariable 으로 댓글 목록을 조회할 게시글의 postId 를 전송해주세요.")
    @GetMapping("/list/{postId}")
    public ApiResponse<CommentResponseDto.GetCommentListResultDto> getCommentList(HttpServletRequest httpServletRequest,
                                                                                  @PathVariable(name = "postId") Long postId) {

        CommentResponseDto.GetCommentListResultDto getCommentListResultDto = commentQueryService.getCommentListResultDto(httpServletRequest, postId);
        if (getCommentListResultDto.getCommentResultDtoList().isEmpty()) {
            return ApiResponse.of(SuccessStatus.COMMENT_LIST_NOT_FOUND, null);
        }

        return ApiResponse.of(SuccessStatus.COMMENT_LIST_FOUND, getCommentListResultDto);
    }


    @PostMapping("/like/{commentId}")
    public ApiResponse<CommentLikeResponseDto.CommentLikeResultDto> likeComment(HttpServletRequest httpServletRequest,
                                                                                @PathVariable(name = "commentId")Long commentId) {
        Boolean isLiked = commentLikeCommandService.likeComment(httpServletRequest, commentId);
        CommentLikeResponseDto.CommentLikeResultDto dto = new CommentLikeResponseDto.CommentLikeResultDto(commentId, commentLikeQueryService.countCommentLike(commentId));

        SuccessStatus status;
        String message;

        Long targetMemberId = commentQueryService.findWriterIdByCommentId(commentId);
        String targetObject = commentQueryService.findCommentBodyByCommentId(commentId);
        if (targetObject.length() > 10) {
            targetObject = targetObject.substring(0, 10);
        }

        if (isLiked) {
            status = SuccessStatus.COMMENT_LIKE;

            String loginMemberNickname = jwtUtils.getMemberNicknameFromRequest(httpServletRequest);

            MemberResponseDto.GetMemberResultDto targetMemberResultDto = memberClient.findMemberByMemberId(targetMemberId);
            String targetMemberNickname = targetMemberResultDto.getNickname();

            message = loginMemberNickname + "님이 회원님의 댓글 \"" + targetObject + "\"에 좋아요를 눌렀습니다. ";
            NotificationMessage notificationMessage = notificationService.createNotificationMessage(targetMemberId, targetMemberNickname, message);

            // Kafka로 메세지 전송
            notificationService.commentLikeNotificationCreate(notificationMessage);
        } else {
            status = SuccessStatus.COMMENT_UNLIKE;
        }

        return ApiResponse.of(status, dto);

    }
}
