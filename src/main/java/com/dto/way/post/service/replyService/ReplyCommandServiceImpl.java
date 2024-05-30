package com.dto.way.post.service.replyService;

import com.dto.way.post.converter.ReplyConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Reply;
import com.dto.way.post.global.exception.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
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
        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Reply reply = ReplyConverter.toReply(loginMemberId, comment, createReplyDto);
        return replyRepository.save(reply);
    }

    @Override
    public ReplyResponseDto.DeleteReplyResultDto deleteReply(HttpServletRequest httpServletRequest, Long replyId) {
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.REPLY_NOT_FOUND));
        ReplyResponseDto.DeleteReplyResultDto deleteReplyResultDto = ReplyConverter.toDeleteReplyResultDto(reply);

        if (loginMemberId.equals(reply.getMemberId())) {
            replyRepository.delete(reply);
        } else {
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }
        return deleteReplyResultDto;
    }

    @Override
    public Reply updateReply(HttpServletRequest httpServletRequest, Long replyId, ReplyRequestDto.UpdateReplyDto updateReplyDto) {
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        Reply reply = replyRepository.findByReplyId(replyId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

        if (loginMemberId.equals(reply.getMemberId())) {
            if (updateReplyDto.getBody() != null) {
                reply.updateBody(updateReplyDto.getBody());
            } else {
                throw new ExceptionHandler(ErrorStatus._EMPTY_FIELD);
            }
        } else {
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return replyRepository.save(reply);
    }

    @Override
    public Long countReply(Long commentId) {

        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

        return replyRepository.countByComment(comment);
    }
}
