package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Reply;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ReplyCommandService {

    Reply createReply(HttpServletRequest httpServletRequest, Long commentId, ReplyRequestDto.CreateReplyDto createReplyDto);

    ReplyResponseDto.DeleteReplyResultDto deleteReply(HttpServletRequest httpServletRequest, Long replyId);

    Reply updateReply(HttpServletRequest httpServletRequest, Long replyId, ReplyRequestDto.UpdateReplyDto updateReplyDto);

    Long countReply(Long commentId);
}
