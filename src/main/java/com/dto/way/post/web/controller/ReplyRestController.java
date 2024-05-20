package com.dto.way.post.web.controller;

import com.dto.way.post.converter.ReplyConverter;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.global.response.ApiResponse;
import com.dto.way.post.global.response.code.status.SuccessStatus;
import com.dto.way.post.service.replyService.ReplyCommandService;
import com.dto.way.post.service.replyService.ReplyQueryService;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
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

    @PostMapping("/{commentId}")
    public ApiResponse<ReplyResponseDto.CreateReplyResultDto> createReply(Authentication auth,
                                                                          @PathVariable(name = "commentId") Long commentId,
                                                                          @Valid @RequestBody ReplyRequestDto.CreateReplyDto request) {

        Reply reply = replyCommandService.createReply(auth, commentId, request);
        return ApiResponse.of(SuccessStatus.REPLY_CREATED, ReplyConverter.toCreateReplyResultDto(reply));
    }

    @DeleteMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.DeleteReplyResultDto> deleteReply(Authentication auth,
                                                                          @PathVariable(name = "replyId") Long replyId) {
        return ApiResponse.of(SuccessStatus.REPLY_DELETED, replyCommandService.deleteReply(auth, replyId));
    }

    @PatchMapping("/{replyId}")
    public ApiResponse<ReplyResponseDto.UpdateReplyResultDto> updateReply(Authentication auth,
                                                                          @PathVariable(name = "replyId") Long replyId,
                                                                          @Valid @RequestBody ReplyRequestDto.UpdateReplyDto request) {
        Reply reply = replyCommandService.updateReply(auth, replyId, request);
        return ApiResponse.of(SuccessStatus.REPLY_UPDATED, ReplyConverter.toUpdateReplyResultDto(reply));
    }

    @GetMapping("/{commentId}")
    public ApiResponse<ReplyResponseDto.GetReplyListResultDto> getReplyList(@PathVariable(name = "commentId") Long commentId) {
        List<Reply> replyList = replyQueryService.getReplyList(commentId);

        return ApiResponse.of(SuccessStatus.REPLY_LIST_FOUND, ReplyConverter.toGetReplyListResultDto(replyList));
    }

}
