package com.dto.way.post.service.commentService;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.exception.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final HistoryRepository historyRepository;
    private final CommentRepository commentRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Comment createComment(HttpServletRequest httpServletRequest, Long postId, CommentRequestDto.CreateCommentDto createCommentDto) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        History history = historyRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.HISTORY_NOT_FOUND));

        Comment comment = CommentConverter.toComment(loginMemberId, history, createCommentDto);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto.DeleteCommentResultDto deleteComment(HttpServletRequest httpServletRequest, Long commentId) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

        CommentResponseDto.DeleteCommentResultDto deleteCommentResultDto = CommentConverter.toDeleteCommentResultDto(comment);
        if (loginMemberId.equals(comment.getMemberId())) {
            commentRepository.delete(comment);
        } else {
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return deleteCommentResultDto;
    }

    @Override
    @Transactional
    public Comment updateComment(HttpServletRequest httpServletRequest, Long commentId, CommentRequestDto.UpdateCommentDto updateCommentDto) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

        if (loginMemberId.equals(comment.getMemberId())) {
            if (updateCommentDto.getBody() != null) {
                comment.updateBody(updateCommentDto.getBody());
            } else {
                throw new ExceptionHandler(ErrorStatus._EMPTY_FIELD);
            }
        } else {
            throw new ExceptionHandler(ErrorStatus._FORBIDDEN);
        }

        return commentRepository.save(comment);
    }

    @Override
    public Long countComment(Long postId) {
        History history = historyRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.HISTORY_NOT_FOUND));

        return commentRepository.countByHistory(history);
    }
}
