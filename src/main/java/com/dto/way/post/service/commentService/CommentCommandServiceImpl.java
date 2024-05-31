package com.dto.way.post.service.commentService;

import com.dto.way.message.NotificationMessage;
import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.exception.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.service.notificationService.NotificationService;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final HistoryRepository historyRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final MemberClient memberClient;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Comment createComment(HttpServletRequest httpServletRequest, Long postId, CommentRequestDto.CreateCommentDto createCommentDto) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        String loginMemberNickname = jwtUtils.getMemberNicknameFromRequest(httpServletRequest);

        History history = historyRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.HISTORY_NOT_FOUND));

        Comment comment = commentRepository.save(CommentConverter.toComment(loginMemberId, history, createCommentDto));

        // 알림을 위한 데이터 세팅
        String targetObject = history.getTitle();
        if (targetObject.length() > 10) {
            targetObject = targetObject.substring(0, 10);
        }
        Long targetMemberId = history.getPost().getMemberId();
        MemberResponseDto.GetMemberResultDto targetMemberResultDto = memberClient.findMemberByMemberId(targetMemberId);
        String targetMemberNickname = targetMemberResultDto.getNickname();
        String noticeMessage = loginMemberNickname + "님이 회원님의 \"" + targetObject + "\"에 댓글을 남겼습니다.";

        NotificationMessage notificationMessage = notificationService.createNotificationMessage(targetMemberId, targetMemberNickname, noticeMessage);

        // Kafka에 알림 전송
        notificationService.commentNotificationCreate(notificationMessage);

        return comment;
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
