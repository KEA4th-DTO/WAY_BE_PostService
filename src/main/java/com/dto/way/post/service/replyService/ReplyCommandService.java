package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Reply;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import org.springframework.security.core.Authentication;

public interface ReplyCommandService {

    Reply createReply(Authentication auth, Long commentId, ReplyRequestDto.CreateReplyDto createReplyDto);

    ReplyResponseDto.DeleteReplyResultDto deleteReply(Authentication auth, Long replyId);

    Reply updateReply(Authentication auth, Long replyId, ReplyRequestDto.UpdateReplyDto updateReplyDto);

    Long countReply(Long commentId);
}
