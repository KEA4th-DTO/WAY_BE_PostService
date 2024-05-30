package com.dto.way.post.web.controller;

import com.dto.way.post.converter.ReplyConverter;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.service.replyService.ReplyCommandService;
import com.dto.way.post.service.replyService.ReplyQueryService;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtUtils jwtUtils;


    @Operation(summary = "대댓글 생성 API", description = "대댓글을 생성하는 API 입니다. PathVariable 으로 대댓들을 작성할 댓글의 commentId, RequestBody 으로 대댓글 내용을 전송해주세요.")
    @PostMapping("/{commentId}")
    public ApiResponse<ReplyResponseDto.CreateReplyResultDto> createReply(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "commentId") Long commentId,
                                                                          @Valid @RequestBody ReplyRequestDto.CreateReplyDto request) {

        Reply reply = replyCommandService.createReply(httpServletRequest, commentId, request);
        return ApiResponse.of(SuccessStatus.REPLY_CREATED, ReplyConverter.toCreateReplyResultDto(reply));
    }

    @Operation(summary = "대댓글 삭제 API", description = "대댓글을 삭제하는 API 입니다. PathVariable 으로 삭제할 대댓글의 replyId를 전송해주세요.")
    @DeleteMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.DeleteReplyResultDto> deleteReply(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "replyId") Long replyId) {
        return ApiResponse.of(SuccessStatus.REPLY_DELETED, replyCommandService.deleteReply(httpServletRequest, replyId));
    }


    @Operation(summary = "대댓글 수정 API", description = "대댓글을 수정하는 API 입니다. PathVariable 으로 수정할 대댓글의 replyId, RequestBody 으로 수정 내용을 전송해주세요.")
    @PatchMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.UpdateReplyResultDto> updateReply(HttpServletRequest httpServletRequest,
                                                                          @PathVariable(name = "replyId") Long replyId,
                                                                          @Valid @RequestBody ReplyRequestDto.UpdateReplyDto request) {
        Reply reply = replyCommandService.updateReply(httpServletRequest, replyId, request);
        return ApiResponse.of(SuccessStatus.REPLY_UPDATED, ReplyConverter.toUpdateReplyResultDto(reply));
    }


    @Operation(summary = "대댓글 상세 조회(단건) API", description = "대댓글을 상세조회하는 API 입니다. PathVariable 으로 상세조회할 대댓글의 replyId 을 전송해주세요.")
    @GetMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.GetReplyResultDto> getReply(HttpServletRequest httpServletRequest,
                                                                    @PathVariable(name = "replyId") Long replyId) {

        ReplyResponseDto.GetReplyResultDto getReplyResultDto = replyQueryService.getReplyResultDto(httpServletRequest, replyId);

        return ApiResponse.of(SuccessStatus.REPLY_FOUND, getReplyResultDto);
    }

    @Operation(summary = "대댓글 목록 조회 API", description = "특정 댓글의 대댓글 목록을 조회하는 API 입니다. PathVariable 으로 대댓글 목록을 조회할 댓글의 comment 을 전송해주세요.")
    @GetMapping("/list/{commentId}")
    public ApiResponse<ReplyResponseDto.GetReplyListResultDto> getReplyList(HttpServletRequest httpServletRequest,
                                                                            @PathVariable(name = "commentId") Long commentId) {

        ReplyResponseDto.GetReplyListResultDto getReplyListResultDto = replyQueryService.getReplyListResultDto(httpServletRequest, commentId);
        if (getReplyListResultDto.getReplyResultDtoList().isEmpty()) {
            return ApiResponse.of(SuccessStatus.REPLY_LIST_NOT_FOUND, null);
        }

        return ApiResponse.of(SuccessStatus.REPLY_LIST_FOUND, getReplyListResultDto);
    }

}
