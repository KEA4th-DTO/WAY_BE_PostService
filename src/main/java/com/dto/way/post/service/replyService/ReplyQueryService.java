package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Reply;

import java.util.List;

public interface ReplyQueryService {

    Reply getReply(Long replyId);

    List<Reply> getReplyList(Long commentId);
}
