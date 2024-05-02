package com.dto.way.post.converter;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.History;
import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;

import java.time.LocalDateTime;

public class CommentConverter {

    public  static Comment toComment(String email, History history, CommentRequestDto.CreateCommentDto request) {
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
}
