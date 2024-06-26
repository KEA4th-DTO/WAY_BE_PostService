package com.dto.way.post.converter;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CommentConverter {

    public static Comment toComment(Long memberId, Post post, CommentRequestDto.CreateCommentDto request) {
        return Comment.builder()
                .memberId(memberId)
                .post(post)
                .body(request.getBody())
                .build();
    }

    public static CommentResponseDto.CreateCommentResultDto toCreateCommentResultDto(Comment comment) {
        return CommentResponseDto.CreateCommentResultDto.builder()
                .commentId(comment.getId())
                .createAt(comment.getCreatedAt()).build();
    }

    public static CommentResponseDto.DeleteCommentResultDto toDeleteCommentResultDto(Comment comment) {
        return CommentResponseDto.DeleteCommentResultDto.builder()
                .commentId(comment.getId())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static CommentResponseDto.UpdateCommentResultDto toUpdateCommentResultDto(Comment comment) {
        return CommentResponseDto.UpdateCommentResultDto.builder()
                .commentId(comment.getId())
                .updatedAt(comment.getUpdatedAt()).build();
    }

//    public static CommentResponseDto.GetCommentResultDto toGetCommentResultDto(Long loginMemberId, Comment comment) {
//
//        Boolean isOwned = comment.getMemberId().equals(loginMemberId);
//
//
//        return CommentResponseDto.GetCommentResultDto.builder()
//                .memberEmail(comment.getMemberEmail())
//                .body(comment.getBody())
//                .isOwned(isOwned)
//                .replyCounts((long) comment.getReplyList().size())
//                .createdAt(comment.getCreatedAt())
//                .build();
//    }
//
//    public static CommentResponseDto.GetCommentListResultDto toGetCommentListResultDto(Long loginMemberId, List<Comment> comments) {
//        List<CommentResponseDto.GetCommentResultDto> commentResultDtoList = comments.stream()
//                .map(comment -> CommentConverter.toGetCommentResultDto(loginMemberId, comment)).collect(Collectors.toList());
//        return CommentResponseDto.GetCommentListResultDto.builder()
//                .commentResultDtoList(commentResultDtoList).build();
//    }
}
