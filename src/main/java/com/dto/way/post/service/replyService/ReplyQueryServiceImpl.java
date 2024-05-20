package com.dto.way.post.service.replyService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyQueryServiceImpl implements ReplyQueryService{

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    @Override
    public Reply getReply(Long replyId) {
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(()->new EntityNotFoundException("대댓글이 존재하지 않습니다."));
        return reply;
    }

    @Override
    public List<Reply> getReplyList(Long commentId) {
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        List<Reply> replyList = replyRepository.findAllByComment(comment);

        return replyList;
    }
}
