package com.dto.way.post.service.replyService;

import com.dto.way.post.converter.ReplyConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.ReplyRepository;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReplyCommandServiceImpl implements ReplyCommandService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Override
    public Reply createReply(Authentication auth, Long commentId, ReplyRequestDto.CreateReplyDto createReplyDto) {

        String email = auth.getName();
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. "));

        Reply reply = ReplyConverter.toReply(email, comment, createReplyDto);
        return replyRepository.save(reply);
    }

    @Override
    public ReplyResponseDto.DeleteReplyResultDto deleteReply(Authentication auth, Long replyId) {
        String email = auth.getName();
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(() -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다."));
        ReplyResponseDto.DeleteReplyResultDto deleteReplyResultDto = ReplyConverter.toDeleteReplyResultDto(reply);

        if (email.equals(reply.getMemberEmail())) {
            replyRepository.delete(reply);
        } else {
            throw new SecurityException("대댓글 작성자만 대댓글을 삭제할 수 있습니다.");
        }
        return deleteReplyResultDto;
    }

    @Override
    public Reply updateReply(Authentication auth, Long replyId, ReplyRequestDto.UpdateReplyDto updateReplyDto) {
        String email = auth.getName();
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(() -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다."));

        if (email.equals(reply.getMemberEmail())) {
            if (updateReplyDto.getBody() != null) {
                reply.updateBody(updateReplyDto.getBody());
            } else {
                throw new IllegalArgumentException("수정할 내용을 입력해주세요.");
            }
        } else {
            throw new SecurityException("대댓글 작성자만 대댓글을 수정할 수 있습니다.");
        }

        return replyRepository.save(reply);
    }

    @Override
    public Long countReply(Long commentId) {

        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        return replyRepository.countByComment(comment);
    }
}
