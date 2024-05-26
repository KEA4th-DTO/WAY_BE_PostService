package com.dto.way.post.service.commentService;

import com.dto.way.post.domain.Comment;
import com.dto.way.post.web.dto.commentDto.CommentRequestDto;
import com.dto.way.post.web.dto.commentDto.CommentResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface CommentCommandService {
    Comment createComment(HttpServletRequest httpServletRequest, Long postId, CommentRequestDto.CreateCommentDto createCommentDto);

    CommentResponseDto.DeleteCommentResultDto deleteComment(HttpServletRequest httpServletRequest, Long commentId);

    Comment updateComment(HttpServletRequest httpServletRequest, Long commentId, CommentRequestDto.UpdateCommentDto updateCommentDto);

    Long countComment(Long postId);
}
