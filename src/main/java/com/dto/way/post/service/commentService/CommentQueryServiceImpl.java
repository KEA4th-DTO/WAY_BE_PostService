package com.dto.way.post.service.commentService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import com.dto.way.post.global.exception.handler.ExceptionHandler;
import com.dto.way.post.global.response.code.status.ErrorStatus;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentLikeRepository;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.PostRepository;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtils jwtUtils;
    private final  MemberClient memberClient;

    @Override
    public CommentResponseDto.GetCommentResultDto getCommentResultDto(HttpServletRequest httpServletRequest, Long postId) {
        Comment comment = commentRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        Boolean isOwned = comment.getMemberId().equals(loginMemberId);

        return CommentResponseDto.GetCommentResultDto.builder()
                .commentId(comment.getId())
                .body(comment.getBody())
                .isOwned(isOwned)
                .replyCounts((long) comment.getReplyList().size())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Override
    public CommentResponseDto.GetCommentListResultDto getCommentListResultDto(HttpServletRequest httpServletRequest, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.POST_NOT_FOUND));

        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto.GetCommentResultDto> commentResultDtoList = commentList.stream()
                .map(comment -> {
                    CommentResponseDto.GetCommentResultDto dto = new CommentResponseDto.GetCommentResultDto();
                    dto.setCommentId(comment.getId());
                    dto.setBody(comment.getBody());
                    dto.setIsOwned(loginMemberId.equals(comment.getMemberId()));
                    dto.setReplyCounts((long) comment.getReplyList().size());
                    dto.setCreatedAt(comment.getCreatedAt());

                    MemberResponseDto.GetMemberResultDto writerMemberInfo = memberClient.findMemberByMemberId(comment.getMemberId());
                    dto.setWriterNickname(writerMemberInfo.getNickname());
                    dto.setWriterProfileImageUrl(writerMemberInfo.getProfileImageUrl());

                    boolean isLiked = commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), loginMemberId);
                    dto.setIsLiked(isLiked);
                    dto.setLikeCounts((long)comment.getCommentLikes().size());
                    return dto;
                })
                .collect(Collectors.toList());


        return CommentResponseDto.GetCommentListResultDto.builder()
                .commentResultDtoList(commentResultDtoList).build();
    }

    @Override
    @Transactional(readOnly = true)
    public Long findWriterIdByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));
        return  comment.getMemberId();
    }

    @Override
    public String findCommentBodyByCommentId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ExceptionHandler(ErrorStatus.COMMENT_NOT_FOUND));
        return comment.getBody();
    }
}
