package com.dto.way.post.service.commentService;

import com.dto.way.post.converter.CommentConverter;
import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.global.utils.JwtUtils;
import com.dto.way.post.repository.CommentRepository;
import com.dto.way.post.repository.HistoryRepository;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import com.dto.way.post.web.dto.memberDto.MemberResponseDto;
import com.dto.way.post.web.feign.MemberClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {
    private final CommentRepository commentRepository;
    private final HistoryRepository historyRepository;
    private final JwtUtils jwtUtils;
    private final  MemberClient memberClient;

    @Override
    public CommentResponseDto.GetCommentResultDto getCommentResultDto(HttpServletRequest httpServletRequest, Long postId) {
        Comment comment = commentRepository.findByCommentId(postId).orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        Boolean isOwned = comment.getMemberId().equals(loginMemberId);

        return CommentResponseDto.GetCommentResultDto.builder()
                .body(comment.getBody())
                .isOwned(isOwned)
                .replyCounts((long) comment.getReplyList().size())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Override
    public CommentResponseDto.GetCommentListResultDto getCommentListResultDto(HttpServletRequest httpServletRequest, Long postId) {

        History history = historyRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Long loginMemberId = jwtUtils.getMemberIdFromRequest(httpServletRequest);

        List<Comment> commentList = commentRepository.findAllByHistory(history);
        List<CommentResponseDto.GetCommentResultDto> commentResultDtoList = commentList.stream()
                .map(comment -> {
                    CommentResponseDto.GetCommentResultDto dto = new CommentResponseDto.GetCommentResultDto();
                    dto.setBody(comment.getBody());
                    dto.setIsOwned(loginMemberId.equals(comment.getMemberId()));
                    dto.setReplyCounts((long) comment.getReplyList().size());
                    dto.setCreatedAt(comment.getCreatedAt());

                    MemberResponseDto.GetMemberResultDto writerMemberInfo = memberClient.findMemberByMemberId(comment.getMemberId());
                    dto.setWriterNickname(writerMemberInfo.getNickname());
                    dto.setWriterProfileImageUrl(writerMemberInfo.getProfileImageUrl());

                    return dto;
                })
                .collect(Collectors.toList());


        return CommentResponseDto.GetCommentListResultDto.builder()
                .commentResultDtoList(commentResultDtoList).build();
    }
}
