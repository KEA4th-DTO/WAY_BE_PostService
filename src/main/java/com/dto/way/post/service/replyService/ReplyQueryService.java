package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Reply;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ReplyQueryService {

    ReplyResponseDto.GetReplyResultDto getReplyResultDto(HttpServletRequest httpServletRequest, Long replyId);

    ReplyResponseDto.GetReplyListResultDto getReplyListResultDto (HttpServletRequest httpServletRequest, Long commentId);

}
