package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Reply;

import java.util.List;

public interface ReplyQueryService {

    List<Reply> getReplyList(Long commentId);
}
