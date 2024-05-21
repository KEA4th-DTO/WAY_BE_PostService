package com.dto.way.post.converter;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static Comment toComment(String email, History history, CommentRequestDto.CreateCommentDto request) {
        return Comment.builder()
                .memberEmail(email)
                .history(history)
                .body(request.getBody())
                .build();
    }

    public static CommentResponseDto.CreateCommentResultDto toCreateCommentResultDto(Comment comment) {
        return CommentResponseDto.CreateCommentResultDto.builder()
                .commentId(comment.getCommentId())
                .createAt(comment.getCreatedAt()).build();
    }

    public static CommentResponseDto.DeleteCommentResultDto toDeleteCommentResultDto(Comment comment) {
        return CommentResponseDto.DeleteCommentResultDto.builder()
                .commentId(comment.getCommentId())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static CommentResponseDto.UpdateCommentResultDto toUpdateCommentResultDto(Comment comment) {
        return CommentResponseDto.UpdateCommentResultDto.builder()
                .commentId(comment.getCommentId())
                .updatedAt(comment.getUpdatedAt()).build();
    }

    public static CommentResponseDto.GetCommentResultDto toGetCommentResultDto(String loginMemberEmail, Comment comment) {

        Boolean isOwned = comment.getMemberEmail().equals(loginMemberEmail);

        return CommentResponseDto.GetCommentResultDto.builder()
                .memberEmail(comment.getMemberEmail())
                .body(comment.getBody())
                .isOwned(isOwned)
                .replyCounts((long) comment.getReplyList().size())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponseDto.GetCommentListResultDto toGetCommentListResultDto(String loginMemberEmail, List<Comment> comments) {
        List<CommentResponseDto.GetCommentResultDto> commentResultDtoList = comments.stream()
                .map(comment -> CommentConverter.toGetCommentResultDto(loginMemberEmail, comment)).collect(Collectors.toList());
        return CommentResponseDto.GetCommentListResultDto.builder()
                .commentResultDtoList(commentResultDtoList).build();
    }
}
