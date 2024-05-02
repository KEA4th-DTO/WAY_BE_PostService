package com.dto.way.post.converter;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.domain.Post;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;

public class CommentConverter {

    public  static Comment toComment(String email, Post post, CommentRequestDto.CreateCommentDto request) {
        return Comment.builder()
                .memberEmail(email)
                .post(post)
                .body(request.getBody())
                .build();
    }

    public static CommentResponseDto.CreateCommentResultDto toCreateCommentResultDto(Comment comment) {
        return CommentResponseDto.CreateCommentResultDto.builder()
                .commentId(comment.getCommentId())
                .createAt(comment.getCreatedAt()).build();
    }
}