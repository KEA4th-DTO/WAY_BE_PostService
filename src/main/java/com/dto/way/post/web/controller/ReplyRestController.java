package com.dto.way.post.web.controller;

import com.dto.way.post.converter.ReplyConverter;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.replyService.ReplyCommandService;
import com.dto.way.post.service.replyService.ReplyQueryService;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post-service/reply")
@RequiredArgsConstructor
public class ReplyRestController {
    private final ReplyCommandService replyCommandService;
    private final ReplyQueryService replyQueryService;

    @Operation(summary = "대댓글 생성 API", description = "대댓글을 생성하는 API 입니다. PathVariable 으로 대댓들을 작성할 댓글의 commentId, RequestBody 으로 대댓글 내용을 전송해주세요.")
    @PostMapping("/{commentId}")
    public ApiResponse<ReplyResponseDto.CreateReplyResultDto> createReply(Authentication auth,
                                                                          @PathVariable(name = "commentId") Long commentId,
                                                                          @Valid @RequestBody ReplyRequestDto.CreateReplyDto request) {

        Reply reply = replyCommandService.createReply(auth, commentId, request);
        return ApiResponse.of(SuccessStatus.REPLY_CREATED, ReplyConverter.toCreateReplyResultDto(reply));
    }

    @Operation(summary = "대댓글 삭제 API", description = "대댓글을 삭제하는 API 입니다. PathVariable 으로 삭제할 대댓글의 replyId를 전송해주세요.")
    @DeleteMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.DeleteReplyResultDto> deleteReply(Authentication auth,
                                                                          @PathVariable(name = "replyId") Long replyId) {
        return ApiResponse.of(SuccessStatus.REPLY_DELETED, replyCommandService.deleteReply(auth, replyId));
    }


    @Operation(summary = "대댓글 수정 API", description = "대댓글을 수정하는 API 입니다. PathVariable 으로 수정할 대댓글의 replyId, RequestBody 으로 수정 내용을 전송해주세요.")
    @PatchMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.UpdateReplyResultDto> updateReply(Authentication auth,
                                                                          @PathVariable(name = "replyId") Long replyId,
                                                                          @Valid @RequestBody ReplyRequestDto.UpdateReplyDto request) {
        Reply reply = replyCommandService.updateReply(auth, replyId, request);
        return ApiResponse.of(SuccessStatus.REPLY_UPDATED, ReplyConverter.toUpdateReplyResultDto(reply));
    }


    @Operation(summary = "대댓글 상세 조회(단건) API", description = "대댓글을 상세조회하는 API 입니다. PathVariable 으로 상세조회할 대댓글의 replyId 을 전송해주세요.")
    @GetMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.GetReplyResultDto> getReply(Authentication auth,
                                                                    @PathVariable(name = "replyId") Long replyId) {
        Reply reply = replyQueryService.getReply(replyId);
        String loginMemberEmail = auth.getName();

        return ApiResponse.of(SuccessStatus.REPLY_FOUND, ReplyConverter.toGetReplyResultDto(loginMemberEmail,reply));
    }

    @Operation(summary = "대댓글 목록 조회 API", description = "특정 댓글의 대댓글 목록을 조회하는 API 입니다. PathVariable 으로 대댓글 목록을 조회할 댓글의 comment 을 전송해주세요.")
    @GetMapping("/list/{commentId}")
    public ApiResponse<ReplyResponseDto.GetReplyListResultDto> getReplyList(Authentication auth,
                                                                            @PathVariable(name = "commentId") Long commentId) {
        List<Reply> replyList = replyQueryService.getReplyList(commentId);
        String loginMemberEmail = auth.getName();

        return ApiResponse.of(SuccessStatus.REPLY_LIST_FOUND, ReplyConverter.toGetReplyListResultDto(loginMemberEmail,replyList));
    }

}
