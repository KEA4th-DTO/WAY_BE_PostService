package com.dto.way.post.service.replyService;

import com.dto.way.post.converter.ReplyConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.ReplyRepository;
import com.dto.way.post.web.dto.replyDto.ReplyRequestDto;
import com.dto.way.post.web.dto.replyDto.ReplyResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReplyCommandServiceImpl implements ReplyCommandService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtils jwtUtils;

    @Override
    public Reply createReply(HttpServletRequest httpServletRequest, Long commentId, ReplyRequestDto.CreateReplyDto createReplyDto) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. "));

        Reply reply = ReplyConverter.toReply(loginMemberId, comment, createReplyDto);
        return replyRepository.save(reply);
    }

    @Override
    public ReplyResponseDto.DeleteReplyResultDto deleteReply(HttpServletRequest httpServletRequest, Long replyId) {
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(() -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다."));
        ReplyResponseDto.DeleteReplyResultDto deleteReplyResultDto = ReplyConverter.toDeleteReplyResultDto(reply);

        if (loginMemberId.equals(reply.getMemberId())) {
            replyRepository.delete(reply);
        } else {
            throw new SecurityException("대댓글 작성자만 대댓글을 삭제할 수 있습니다.");
        }
        return deleteReplyResultDto;
    }

    @Override
    public Reply updateReply(HttpServletRequest httpServletRequest, Long replyId, ReplyRequestDto.UpdateReplyDto updateReplyDto) {
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(() -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다."));

        if (loginMemberId.equals(reply.getMemberId())) {
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
