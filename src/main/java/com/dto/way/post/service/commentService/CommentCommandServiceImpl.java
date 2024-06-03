package com.dto.way.post.service.commentService;

import com.dto.way.message.NotificationMessage;
import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import com.dto.way.post.domain.enums.PostType;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.service.notificationService.NotificationService;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final MemberClient memberClient;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Comment createComment(HttpServletRequest httpServletRequest, Long postId, CommentRequestDto.CreateCommentDto createCommentDto) {

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);
        String loginMemberNickname = jwtUtils.getMemberNicknameFromRequest(httpServletRequest);

        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.POST_NOT_FOUND));

        Comment comment = commentRepository.save(CommentConverter.toComment(loginMemberId, post, createCommentDto));

        // 알림을 위한 데이터 세팅
        String targetObject = post.getPostType().equals(PostType.DAILY) ? post.getDaily().getTitle() : post.getHistory().getTitle();
        if (targetObject.length() > 10) {
            targetObject = targetObject.substring(0, 10);
        }
        Long targetMemberId = post.getMemberId();
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

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

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

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));

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
        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.POST_NOT_FOUND));

        return commentRepository.countByPost(post);
    }
}
